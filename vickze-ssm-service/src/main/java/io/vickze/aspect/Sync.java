package io.vickze.aspect;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.locks.Lock;

import io.vickze.lock.RedisLock;

/**
 * 使用此注解给方法加分布式锁
 *
 * @author vick.zeng
 * @email zyk@yk95.top
 * @date 2017-12-07 16:24
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Sync {

    /**
     * 锁命名空间
     */
    String lockNameSpace() default "";
    /**
     * 锁值
     */
    String lockKey() default "";

    /**
     * 锁类型 (redis锁、zookeeper锁）
     */
    Class<? extends Lock> lockClass() default RedisLock.class;

    /**
     *
     */
    boolean isBaseDO() default false;

    /**
     *
     */
    int baseDOIndex() default 0;
}
