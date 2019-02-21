package io.vickze.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import io.vickze.shiro.cache.KryoSerializer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author vick.zeng
 * @email zengyukang@hey900.com
 * @date 2018-02-08 18:31
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ShiroTest {
    @Autowired
    private JedisPool jedisPool;

    @Test
    public void test() {
        Jedis jedis = jedisPool.getResource();
        Set<String> keys = jedis.keys("shiro:cache:*");

        Collection<Object> values = keys.stream().map(key -> {
            Object value = KryoSerializer.deserialize(jedis.get(key.getBytes()));
            return value;
        }).collect(Collectors.toSet());
        jedis.close();

    }
}