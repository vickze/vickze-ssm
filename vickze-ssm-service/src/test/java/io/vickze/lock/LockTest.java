package io.vickze.lock;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.locks.Lock;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * @author vick.zeng
 * @email zengyukang@hey900.com
 * @date 2018-01-03 18:01
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class LockTest {
    @Autowired
    private ShardedJedisPool shardedJedisPool;

    private ShardedJedis shardedJedis;

    private final static String REDIS_LOCK_KEY = "lock_test_stock";

    @Before
    public void before() {
        shardedJedis = shardedJedisPool.getResource();
        shardedJedis.set(REDIS_LOCK_KEY, "500");
    }

    @After
    public void after() {
        shardedJedis.del(REDIS_LOCK_KEY);
        shardedJedis.close();
    }

    @Test
    public void redisLockTest() throws InterruptedException {
        // 多线程测试
        for (int i = 0; i < 510; i++) {
            Lock redisLock = new RedisLock(shardedJedisPool, "lock", "test");

            new Thread(() -> {
                long startTime = System.currentTimeMillis();
                ShardedJedis jedis = shardedJedisPool.getResource();
                try {
                    if (redisLock.tryLock()) {
                        Integer x = Integer.parseInt(jedis.get(REDIS_LOCK_KEY));
                        if (x > 0) {
                            System.out.println("剩余库存:" + x);
                            --x;
                            shardedJedis.set(REDIS_LOCK_KEY, x.toString());
                        } else {
                            System.out.println("没有更多库存了");
                        }
                    } else {
                        System.out.println("未能拿到锁");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    jedis.close();
                    redisLock.unlock();
                }
                System.out.println("花费：" + (System.currentTimeMillis() - startTime) + "ms");
            }).start();
        }

        // 主线程休眠20秒，不然主线程结束子线程不会执行
        Thread.sleep(60000);
    }


    @Test
    public void zookeeperLockTest() throws InterruptedException {
        // 多线程测试
        for (int i = 0; i < 510; i++) {
            Lock zookeeperLock = new ZookeeperLock("127.0.0.1:2181", 3000, "lock", "test");

            new Thread(() -> {
                long startTime = System.currentTimeMillis();
                ShardedJedis jedis = shardedJedisPool.getResource();
                try {
                    if (zookeeperLock.tryLock()) {
                        Integer x = Integer.parseInt(jedis.get(REDIS_LOCK_KEY));
                        if (x > 0) {
                            System.out.println("剩余库存:" + x);
                            --x;
                            shardedJedis.set(REDIS_LOCK_KEY, x.toString());
                        } else {
                            System.out.println("没有更多库存了");
                        }
                    } else {
                        System.out.println("未能拿到锁");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    jedis.close();
                    zookeeperLock.unlock();
                }
                System.out.println("花费：" + (System.currentTimeMillis() - startTime) + "ms");
            }).start();
        }

        // 主线程休眠20秒，不然主线程结束子线程不会执行
        Thread.sleep(60000);
    }
}
