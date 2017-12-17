package io.vickze.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.vickze.aspect.Lock;
import io.vickze.constant.TokenConstant;
import io.vickze.constant.UserConstant;
import io.vickze.entity.TokenDO;
import io.vickze.service.TokenService;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author vick.zeng
 * @email zyk@yk95.top
 * @date 2017-12-12 15:32
 */
@Service
public class TokenServiceImpl implements TokenService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String LOCK_USER_TOKEN_KEY = "lock:user:token:{0}";

    @Autowired
    private JedisPool jedisPool;

    @Override
    public long getUserIdByToken(String token) {
        Jedis jedis = jedisPool.getResource();
        String key = MessageFormat.format(TokenConstant.TOKEN_KEY, token);
        String json = jedis.get(key);

        return json == null ? UserConstant.UN_LOGIN : Long.parseLong(json);
    }

    @Override
    @Lock(LOCK_USER_TOKEN_KEY)
    public TokenDO generateToken(long userId) {
        String token = generateValue();
        String refreshToken = generateValue();

        Jedis jedis = jedisPool.getResource();
        String userKey = MessageFormat.format(TokenConstant.USER_KEY, userId);
        String tokenKey = MessageFormat.format(TokenConstant.TOKEN_KEY, token);
        String refreshTokenKey = MessageFormat.format(TokenConstant.REFRESH_TOKEN_KEY, refreshToken);

        long tokenExpire = TimeoutUtils.toMillis(1, TimeUnit.DAYS);
        long refreshTokenExpire = TimeoutUtils.toMillis(15, TimeUnit.DAYS);

        //用户重新登录，移除旧token
        removeOldToken(userKey, jedis);
        //保存用户token信息到缓存，实现单点登录功能
        jedis.hset(userKey, TokenConstant.USER_TOKEN_HASH_KEY,  token);
        jedis.hset(userKey, TokenConstant.USER_REFRESH_TOKEN_HASH_KEY,  refreshToken);

        //token过期时间为1天
        jedis.set(tokenKey, String.valueOf(userId), "NX", "PX", tokenExpire);
        //refreshToken过期时间为15天，超过15天用户需重新登录
        jedis.set(refreshTokenKey, String.valueOf(userId), "NX", "PX", refreshTokenExpire);

        jedis.close();

        TokenDO tokenDO = new TokenDO();
        tokenDO.setToken(token);
        tokenDO.setRefreshToken(refreshToken);
        tokenDO.setTokenExpire(tokenExpire);
        tokenDO.setRefreshTokenExpire(refreshTokenExpire);

        return tokenDO;
    }

    @Override
    public void removeUserToken(long userId) {
        Jedis jedis = jedisPool.getResource();
        String userKey = MessageFormat.format(TokenConstant.USER_KEY, userId);

        removeOldToken(userKey, jedis);

        jedis.close();
    }

    /**
     * 用户重新登录，移除旧token
     */
    private void removeOldToken(String userKey, Jedis jedis) {
        String oldToken = jedis.hget(userKey, TokenConstant.USER_TOKEN_HASH_KEY);
        String oldRefreshToken = jedis.hget(userKey, TokenConstant.USER_REFRESH_TOKEN_HASH_KEY);

        if (oldToken != null) {
            jedis.del(MessageFormat.format(TokenConstant.TOKEN_KEY, oldToken));
            jedis.hdel(userKey, TokenConstant.USER_TOKEN_HASH_KEY);
        }
        if (oldRefreshToken != null) {
            jedis.del(MessageFormat.format(TokenConstant.REFRESH_TOKEN_KEY, oldRefreshToken));
            jedis.hdel(userKey, TokenConstant.USER_REFRESH_TOKEN_HASH_KEY);
        }
    }


    /**
     * 生成随机数
     */
    private String generateValue() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
