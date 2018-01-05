package io.vickze.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import io.vickze.exception.CheckException;
import io.vickze.lock.RedisLock;
import redis.clients.jedis.ShardedJedisPool;

/**
 * @author vick.zeng
 * @email zyk@yk95.top
 * @date 2017-12-07 16:25
 */
@Aspect
@Component
public class SyncAspect {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ShardedJedisPool shardedJedisPool;

    @Pointcut("@annotation(io.vickze.aspect.Sync)")
    public void lockPointcut() {

    }

    @Around("lockPointcut()")
    public Object arount(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        //拿到接口方法
        Method method = signature.getMethod();
        Sync sync = method.getAnnotation(Sync.class);

        Object[] args = point.getArgs();
        String lockKey = MessageFormat.format(sync.lockKey(), args);
        java.util.concurrent.locks.Lock redisLock = new RedisLock(shardedJedisPool, sync.lockNameSpace(), lockKey);

        long startTime = System.currentTimeMillis();
        try {
            //
            logger.debug(MessageFormat.format("尝试加锁，锁值：{0}", lockKey));
            // 加锁
            if (redisLock.tryLock()) {
                // 拿到锁
                logger.debug(MessageFormat.format("成功拿到锁，锁值：{0}", lockKey));
                logger.debug(MessageFormat.format("耗时：{0}ms", System.currentTimeMillis() - startTime));
                return point.proceed();
            } else {
                // 没能拿到锁
                logger.debug(MessageFormat.format("未能拿到锁，锁值：{0}", lockKey));
                logger.debug(MessageFormat.format("耗时：{0}ms", System.currentTimeMillis() - startTime));
                throw new CheckException("服务器繁忙，请重试！");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            // 释放锁
            redisLock.unlock();
        }
    }
}