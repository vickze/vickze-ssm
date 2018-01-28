package io.vickze.service.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.text.MessageFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.vickze.aspect.Sync;
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
public class TokenServiceImpl implements TokenService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${jwt.secret}")
    private String secret;

    private static final String LOCK_USER_TOKEN_NAMESPACE = "lock:user:token";
    private static final String LOCK_USER_TOKEN_KEY = "{0}";

    /**
     * Jwt过期时间6小时
     */
    private static final long EXPIRE = TimeUnit.HOURS.toMillis(6);
    /**
     * 刷新Jwt过期时间15天
     */
    private static final long REFRESH_EXPIRE = TimeUnit.DAYS.toMillis(15);

    @Autowired
    private ShardedJedisPool shardedJedisPool;

    @Override
    public long validToken(String token) {
        return validToken(token, false);
    }

    @Override
    @Sync(lockNameSpace = LOCK_USER_TOKEN_NAMESPACE, lockKey = LOCK_USER_TOKEN_KEY)
    public TokenDO generateToken(long userId) {
        String token = genJwt(userId);

        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        String userKey = MessageFormat.format(TokenConstant.USER_KEY, userId);
        //保存用户token信息到缓存，实现单端登录功能
        shardedJedis.hset(userKey, TokenConstant.USER_TOKEN_HASH_KEY, token);
        shardedJedis.close();

        TokenDO tokenDO = new TokenDO();
        tokenDO.setToken(token);
        tokenDO.setExpire(EXPIRE);

        return tokenDO;
    }

    @Override
    public void removeUserToken(long userId) {
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        String userKey = MessageFormat.format(TokenConstant.USER_KEY, userId);
        shardedJedis.hdel(userKey, TokenConstant.USER_TOKEN_HASH_KEY);
        shardedJedis.close();
    }

    @Override
    public long validTokenCanRefresh(String token) {
        return validToken(token, true);
    }

    /**
     * 校验token
     */
    private long validToken(String token, boolean refresh) {
        try {
            Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
            long userId = Long.parseLong(claims.getSubject());
            ShardedJedis shardedJedis = shardedJedisPool.getResource();
            String userKey = MessageFormat.format(TokenConstant.USER_KEY, userId);

            //token与当前用户token不一致
            if (!token.equals(shardedJedis.hget(userKey, TokenConstant.USER_TOKEN_HASH_KEY))) {
                shardedJedis.close();
                //该token失效，存在重复登录
                return UserConstant.UN_LOGIN;
            }
            shardedJedis.close();

            Date now = new Date();
            Date expire;
            if (!refresh) {
                //token过期时间
                expire = claims.getExpiration();
            } else {
                //token刷新过期时间
                expire = new Date((long) claims.get("ref"));
            }

            //jwt签发时间早于现在以及过期时间晚于现在，token有效
            if (claims.getIssuedAt().before(now) && expire.after(now)) {
                //返回用户ID
                return userId;
            }

            return UserConstant.UN_LOGIN;
        } catch (Exception e) {
            logger.debug(e.getMessage(), e);
            return UserConstant.UN_LOGIN;
        }
    }

    /**
     * 生成Jwt
     */
    private String genJwt(long userId) {
        Date now = new Date();

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + EXPIRE))
                .claim("ref", new Date(now.getTime() + REFRESH_EXPIRE))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
}
