package io.vickze.lock;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import redis.clients.jedis.ShardedJedisPool;

/**
 * @author vick.zeng
 * @email zengyukang@hey900.com
 * @date 2018-01-03 18:01
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class LockTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ShardedJedisPool shardedJedisPool;

    private int stock = 500;

    private final int threads = 510;

    private volatile int i = 0;

    private volatile int j = 0;

    private volatile int k = 0;

    private volatile int l = 0;

    private CountDownLatch countDownLatch = new CountDownLatch(threads);

    @Before
    public void before() {
    }

    @After
    public void after() {
    }

    @Test
    public void redisLockTest() throws InterruptedException {
        // 多线程测试
        for (int n = 0; n < threads; n++) {
            new Thread(() -> {
                Lock redisLock = new RedisLock(shardedJedisPool, "lock", "doc");

                long startTime = System.currentTimeMillis();
                try {
                    if (redisLock.tryLock(10, TimeUnit.SECONDS)) {
                        if (stock > 0) {
                            stock--;
                            i++;
                            logger.debug("剩余库存:{}", stock);
                        } else {
                            j++;
                        }
                    } else {
                        k++;
                    }
                    logger.debug("花费：{}ms", System.currentTimeMillis() - startTime);
                } catch (Exception e) {
                    l++;
                    e.printStackTrace();
                } finally {
                    redisLock.unlock();
                    countDownLatch.countDown();
                }
            }).start();
        }

        // 主线程休眠，不然主线程结束子线程不会执行
        countDownLatch.await();

        logger.debug("已减库存 {}", i);
        logger.debug("没有更多库存了 {}", j);
        logger.debug("未能拿到锁 {}", k);
        logger.debug("获取锁异常 {}", l);

        if (i + j + k + l == threads) {
            logger.debug("成功锁住代码块");
        } else {
            logger.error("未能锁住代码块");
        }
    }


    @Test
    public void zookeeperLockTest() throws InterruptedException {
        // 多线程测试
        for (int n = 0; n < threads; n++) {
            new Thread(() -> {
                Lock zookeeperLock = new ZookeeperLock("127.0.0.1:2181", 10000, "lock", "doc");

                long startTime = System.currentTimeMillis();
                try {
                    if (zookeeperLock.tryLock(10, TimeUnit.SECONDS)) {
                        if (stock > 0) {
                            stock--;
                            i++;
                            logger.debug("剩余库存:{}", stock);
                        } else {
                            j++;
                        }
                    } else {
                        k++;
                    }
                    logger.debug("花费：{}ms", System.currentTimeMillis() - startTime);
                } catch (Exception e) {
                    l++;
                    e.printStackTrace();
                } finally {
                    zookeeperLock.unlock();
                    countDownLatch.countDown();
                }
            }).start();
        }

        // 主线程休眠，不然主线程结束子线程不会执行
        countDownLatch.await();

        logger.debug("已减库存 {}", i);
        logger.debug("没有更多库存了 {}", j);
        logger.debug("未能拿到锁 {}", k);
        logger.debug("获取锁异常 {}", l);

        if (i + j + k + l == threads) {
            logger.debug("成功锁住代码块");
        } else {
            logger.error("未能锁住代码块");
        }
    }
}
