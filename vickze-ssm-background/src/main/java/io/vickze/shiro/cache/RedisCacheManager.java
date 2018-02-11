package io.vickze.shiro.cache;

import org.apache.shiro.cache.AbstractCacheManager;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.JedisPool;

/**
 * @author vick.zeng
 * @email zengyukang@hey900.com
 * @date 2018-02-08 15:25
 */
@Component
public class RedisCacheManager extends AbstractCacheManager {
    @Autowired
    private JedisPool jedisPool;

    @Override
    protected Cache createCache(String name) throws CacheException {
        return new RedisCache<>(name, jedisPool);
    }
}
