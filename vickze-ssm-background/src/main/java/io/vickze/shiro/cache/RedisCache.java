package io.vickze.shiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;


import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author vick.zeng
 * @email zengyukang@hey900.com
 * @date 2018-01-30 17:04
 */
public class RedisCache<K, V> implements Cache<K, V> {
    private JedisPool jedisPool;

    private final String name;

    private final String prefix = "shiro:cache:";

    public RedisCache(String name, JedisPool jedisPool) {
        if (name == null) {
            throw new IllegalArgumentException("Cache name cannot be null.");
        }
        this.name = name;
        this.jedisPool = jedisPool;
    }

    public V get(K key) throws CacheException {
        Jedis jedis = jedisPool.getResource();
        V value = KryoSerializer.deserialize(jedis.get(genKey(key)));
        jedis.close();
        return value;
    }

    @Override
    public V put(K key, V value) throws CacheException {
        Jedis jedis = jedisPool.getResource();
        jedis.set(genKey(key), genValue(value));
        jedis.close();
        return value;
    }

    @Override
    public V remove(K key) throws CacheException {
        V value = get(key);
        Jedis jedis = jedisPool.getResource();
        jedis.del(genKey(key));
        jedis.close();
        return value;
    }

    @Override
    public void clear() throws CacheException {
        Jedis jedis = jedisPool.getResource();
        Set<String> keys = jedis.keys(prefix + "*");
        for (String key : keys) {
            jedis.del(key);
        }
        jedis.close();
    }

    @Override
    public int size() {
        Jedis jedis = jedisPool.getResource();
        Set<String> keys = jedis.keys(prefix + "*");
        jedis.close();
        return keys.size();
    }

    @Override
    public Set<K> keys() {
        Jedis jedis = jedisPool.getResource();
        Set<String> keys = jedis.keys(prefix + "*");
        jedis.close();

        if (keys.isEmpty()) {
            return Collections.emptySet();
        }

        Set<K> returnKeys = keys.stream().map(key -> (K) key).collect(Collectors.toSet());
        return Collections.unmodifiableSet(returnKeys);
    }

    @Override
    public Collection<V> values() {
        Jedis jedis = jedisPool.getResource();
        Set<String> keys = jedis.keys(prefix + "*");
        if (keys.isEmpty()) {
            return Collections.emptySet();
        }

        if (keys.isEmpty()) {
            return Collections.emptySet();
        }

        Collection<V> values = keys.stream().map(key -> {
            V value = KryoSerializer.deserialize(jedis.get(key.getBytes()));
            return value;
        }).collect(Collectors.toSet());
        jedis.close();

        return Collections.unmodifiableCollection(values);
    }

    private byte[] genKey(K key) {
        return (prefix + key).getBytes();
    }

    private byte[] genValue(V value) {
        return KryoSerializer.serialize(value);
    }
}
