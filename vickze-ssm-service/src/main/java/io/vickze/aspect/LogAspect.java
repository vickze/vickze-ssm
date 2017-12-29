package io.vickze.aspect;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.text.MessageFormat;

import io.vickze.util.JsonUtil;


/**
 * @author vick.zeng
 * @email zyk@yk95.top
 * @date 2017-09-29 15:56
 */
@Aspect
@Component
public class LogAspect {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Pointcut("execution(* io.vickze.service.*.*(..))")
    public void logPointcut() {

    }

    @Around("logPointcut()")
    public Object arount(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        String classMethodName = MessageFormat.format("{0}.{1}()",
                point.getTarget().getClass().getName(), signature.getName());

        Object[] args = point.getArgs();
        logger.debug(MessageFormat.format("{0} start...", classMethodName));
        logger.debug(MessageFormat.format("{0} args：{1}", classMethodName, JsonUtil.toJson(args)));

        long beginTime = System.currentTimeMillis();
        try {
            Object result = point.proceed();
            logger.debug(MessageFormat.format("{0} result：{1}", classMethodName, JsonUtil.toJson(result)));
            return result;
        } catch (Exception e) {
            throw e;
        } finally {
            long endTime = System.currentTimeMillis();
            logger.debug(MessageFormat.format("{0} end... cost:{1}ms", classMethodName, endTime - beginTime));
        }
    }
}
