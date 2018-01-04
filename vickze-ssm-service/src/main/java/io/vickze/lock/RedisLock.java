package io.vickze.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.TimeoutUtils;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import io.vickze.exception.CheckException;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * Redis简易分布式锁
 * setNx实现
 *
 * @author vick.zeng
 * @email zyk@yk95.top
 * @date 2017-12-07 16:27
 */
public class RedisLock implements Lock {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ShardedJedisPool shardedJedisPool;


    /**
     * 锁ID
     */
    private final String lockId;

    /**
     * 锁key值
     */
    private final String lockKey;

    /**
     * 锁超时时间，防止线程在入锁以后，无限的执行等待，默认30秒
     */
    private static final long EXPIRE_SECS = 30;

    /**
     * 锁等待时间，防止线程饥饿，默认10秒
     */
    private static final long TIMEOUT_SECS = 10;

    /**
     * 随机等待时间最小值
     */
    private static final int MIN_RANDOM_SECS = 10;

    /**
     * 随机等待时间最大值
     */
    private static final int MAX_RANDOM_SECS = 300;


    /**
     * 是否持有锁
     */
    private boolean locked = false;


    public RedisLock(ShardedJedisPool shardedJedisPool, String lockKey) {
        this.lockId = UUID.randomUUID().toString();
        this.shardedJedisPool = shardedJedisPool;
        this.lockKey = lockKey;
    }

    @Override
    public void lock() {
        if (this.tryLock()) {
            return;
        }
        throw new CheckException("服务器繁忙，请重试");
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        this.lock();
    }

    @Override
    public boolean tryLock() {
        try {
            return this.tryLock(TIMEOUT_SECS, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.warn(e.getMessage());
            return Boolean.FALSE;
        }
    }

    /**
     * 获得锁
     * 实现思路：使用了redis的set nx expire命令，缓存锁
     * 执行过程：
     * 通过setNx尝试设置某个key的值，成功（当前没有这个锁）则返回，成功获得锁
     *
     * @param time 锁等待时间毫秒
     * @return
     * @throws InterruptedException
     */
    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        long timeout = time;
        while (timeout >= 0) {
            if (setNxAndExpire(lockKey, lockId, EXPIRE_SECS)) {
                // 获得锁
                locked = Boolean.TRUE;
                return Boolean.TRUE;
            }

            // 生成[10-200]区间的随机毫秒
            long delayMills = generateRandomMills(MIN_RANDOM_SECS, MAX_RANDOM_SECS);
            timeout -= delayMills;
            /*
                延迟随机毫秒,防止饥饿进程的出现,即,当同时到达多个进程,只会有一个进程获得锁,其他的都用同样的频率进行尝试,
                后面有来了一些进行,也以同样的频率申请锁,这将可能导致前面来的锁得不到满足.
                使用随机的等待时间可以一定程度上保证公平性
             */
            Thread.sleep(delayMills);
        }

        return Boolean.FALSE;
    }

    /**
     * 释放锁
     */
    public void unlock() {
        if (locked) {
            ShardedJedis shardedJedis = shardedJedisPool.getResource();
            //避免删除非自己获取得到的锁
            if (lockId.equals(shardedJedis.get(lockKey))) {
                shardedJedis.del(lockKey);
            }
            shardedJedis.close();
            locked = Boolean.FALSE;
        }
    }

    @Override
    public Condition newCondition() {
        return null;
    }

    /**
     * 生成[min - max]区间的随机毫秒
     *
     * @param min
     * @param max
     * @return
     */
    private long generateRandomMills(int min, int max) {
        Random random = new Random();
        // randNumber 将被赋值为一个 MIN 和 MAX 范围内的随机数
        return random.nextInt(max - min + 1) + min;
    }

    /**
     * setNX命令不支持expire，所以使用set命令，同时使用nx与expire
     *
     * @param key
     * @param value
     * @param expire 毫秒
     * @return
     */
    private boolean setNxAndExpire(final String key, final String value, final long expire) {
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        String result = shardedJedis.set(key, value, "NX", "PX", expire);
        shardedJedis.close();
        return "OK".equals(result);
    }
}