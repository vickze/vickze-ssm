package io.vickze.config;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vick.zeng
 * @email zyk@yk95.top
 * @date 2017-12-07 17:27
 */
@Configuration
public class RedisConfig {
    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.hosts}")
    private String hosts;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.timeout}")
    private int timeout;

    @Value("${spring.redis.pool.max-active}")
    private int maxActive;

    @Value("${spring.redis.pool.max-wait}")
    private long maxWait;

    @Value("${spring.redis.pool.max-idle}")
    private int maxIdle;

    @Value("${spring.redis.pool.min-idle}")
    private int minIdle;

    @Bean
    public JedisPool jedisPool() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(maxActive);
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWait);
        jedisPoolConfig.setMinIdle(minIdle);

        if (StringUtils.isBlank(password)) {
            return new JedisPool(jedisPoolConfig, host, port, timeout);
        }
        return new JedisPool(jedisPoolConfig, host, port, timeout, password);
    }

    @Bean
    public ShardedJedisPool shardedJedisPool() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(maxActive);
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWait);
        jedisPoolConfig.setMinIdle(minIdle);

        List<JedisShardInfo> jedisShardInfoList = new ArrayList<>();
        String[] hostList = hosts.split(",");

        for (String host : hostList) {
            JedisShardInfo jedisShardInfo = new JedisShardInfo(host);
            //连接超时
            jedisShardInfo.setConnectionTimeout(timeout);
            //读取超时
            jedisShardInfo.setSoTimeout(timeout);
            if (!StringUtils.isBlank(password)) {
                jedisShardInfo.setPassword(password);
            }

            jedisShardInfoList.add(jedisShardInfo);
        }

        return new ShardedJedisPool(jedisPoolConfig, jedisShardInfoList);
    }
}
