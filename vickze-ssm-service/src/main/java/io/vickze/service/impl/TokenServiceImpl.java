package io.vickze.service.impl;

import com.alibaba.dubbo.config.annotation.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.TimeoutUtils;

import java.text.MessageFormat;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.vickze.aspect.Lock;
import io.vickze.constant.TokenConstant;
import io.vickze.constant.UserConstant;
import io.vickze.entity.TokenDO;
import io.vickze.service.TokenService;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * @author vick.zeng
 * @email zyk@yk95.top
 * @date 2017-12-12 15:32
 */
@Service(interfaceClass = TokenService.class)
public class TokenServiceImpl implements TokenService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String LOCK_USER_TOKEN_KEY = "lock:user:token:{0}";

    @Autowired
    private ShardedJedisPool shardedJedisPool;

    @Override
    public long getUserIdByToken(String token) {
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        String key = MessageFormat.format(TokenConstant.TOKEN_KEY, token);
        String json = shardedJedis.get(key);
        shardedJedis.close();

        return json == null ? UserConstant.UN_LOGIN : Long.parseLong(json);
    }

    @Override
    @Lock(LOCK_USER_TOKEN_KEY)
    public TokenDO generateToken(long userId) {
        String token = generateValue();
        String refreshToken = generateValue();

        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        String userKey = MessageFormat.format(TokenConstant.USER_KEY, userId);
        String tokenKey = MessageFormat.format(TokenConstant.TOKEN_KEY, token);
        String refreshTokenKey = MessageFormat.format(TokenConstant.REFRESH_TOKEN_KEY, refreshToken);

        long tokenExpire = TimeoutUtils.toMillis(1, TimeUnit.DAYS);
        long refreshTokenExpire = TimeoutUtils.toMillis(15, TimeUnit.DAYS);

        //用户重新登录，移除旧token
        removeOldToken(userKey, shardedJedis);
        //保存用户token信息到缓存，实现单点登录功能
        shardedJedis.hset(userKey, TokenConstant.USER_TOKEN_HASH_KEY,  token);
        shardedJedis.hset(userKey, TokenConstant.USER_REFRESH_TOKEN_HASH_KEY,  refreshToken);

        //token过期时间为1天
        shardedJedis.set(tokenKey, String.valueOf(userId), "NX", "PX", tokenExpire);
        //refreshToken过期时间为15天，超过15天用户需重新登录
        shardedJedis.set(refreshTokenKey, String.valueOf(userId), "NX", "PX", refreshTokenExpire);

        shardedJedis.close();

        TokenDO tokenDO = new TokenDO();
        tokenDO.setToken(token);
        tokenDO.setRefreshToken(refreshToken);
        tokenDO.setTokenExpire(tokenExpire);
        tokenDO.setRefreshTokenExpire(refreshTokenExpire);

        return tokenDO;
    }

    @Override
    public void removeUserToken(long userId) {
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        String userKey = MessageFormat.format(TokenConstant.USER_KEY, userId);

        removeOldToken(userKey, shardedJedis);

        shardedJedis.close();
    }

    /**
     * 用户重新登录，移除旧token
     */
    private void removeOldToken(String userKey, ShardedJedis shardedJedis) {
        String oldToken = shardedJedis.hget(userKey, TokenConstant.USER_TOKEN_HASH_KEY);
        String oldRefreshToken = shardedJedis.hget(userKey, TokenConstant.USER_REFRESH_TOKEN_HASH_KEY);

        if (oldToken != null) {
            shardedJedis.del(MessageFormat.format(TokenConstant.TOKEN_KEY, oldToken));
            shardedJedis.hdel(userKey, TokenConstant.USER_TOKEN_HASH_KEY);
        }
        if (oldRefreshToken != null) {
            shardedJedis.del(MessageFormat.format(TokenConstant.REFRESH_TOKEN_KEY, oldRefreshToken));
            shardedJedis.hdel(userKey, TokenConstant.USER_REFRESH_TOKEN_HASH_KEY);
        }
    }


    /**
     * 生成随机数
     */
    private String generateValue() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
