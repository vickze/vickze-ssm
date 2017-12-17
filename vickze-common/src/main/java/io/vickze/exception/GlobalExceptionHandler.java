package io.vickze.exception;

import io.vickze.entity.ResultDO;

import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * 异常处理器
 *
 * @author vick.zeng
 * @email 2512522383@qq.com
 * @create 2017-09-08 22:07
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 自定义异常
     */
    @ExceptionHandler(CheckException.class)
    public ResultDO handleDefaultException(CheckException e) {
        return ResultDO.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResultDO handleDuplicateKeyException(DuplicateKeyException e) {
        logger.error(e.getMessage(), e);
        return ResultDO.error("数据库中已存在该记录");
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResultDO handleAuthorizationException(AuthorizationException e) {
        return ResultDO.error("没有权限，请联系管理员授权");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultDO handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResultDO.error(e.getBindingResult().getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResultDO handleException(Exception e) {
        logger.error(e.getMessage(), e);
        return ResultDO.error();
    }

}
