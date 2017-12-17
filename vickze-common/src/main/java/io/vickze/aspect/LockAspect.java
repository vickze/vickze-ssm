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
import redis.clients.jedis.JedisPool;

/**
 * @author vick.zeng
 * @email zyk@yk95.top
 * @date 2017-12-07 16:25
 */
@Aspect
@Component
public class LockAspect {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private JedisPool jedisPool;

    @Pointcut("@annotation(io.vickze.aspect.Lock)")
    public void lockPointcut() {

    }

    @Around("lockPointcut()")
    public Object arount(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        //拿到接口方法
        Method method = signature.getMethod();
        Lock lock = method.getAnnotation(Lock.class);

        Object[] args = point.getArgs();
        String lockKey = MessageFormat.format(lock.value(), args);
        RedisLock redisLock = RedisLock.getLock(jedisPool, lockKey);

        long startTime = System.currentTimeMillis();
        try {
            //
            logger.debug(MessageFormat.format("尝试加锁，锁值：{0}", lockKey));
            // 加锁
            if (redisLock.lock()) {
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