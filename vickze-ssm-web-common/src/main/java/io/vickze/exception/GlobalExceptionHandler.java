package io.vickze.exception;

import io.vickze.entity.ResultDO;

import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * 异常处理器
 *
 * @author vick.zeng
 * @email zyk@yk95.top
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

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthorizationException.class)
    public ResultDO handleAuthorizationException(AuthorizationException e) {
        return ResultDO.error(HttpStatus.UNAUTHORIZED.value(), "没有权限，请联系管理员授权");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultDO handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResultDO.error(e.getBindingResult().getFieldError().getDefaultMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResultDO handleNoHandlerFoundException(NoHandlerFoundException e) {
        logger.debug(e.getMessage());
        return ResultDO.error(HttpStatus.NOT_FOUND.value(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResultDO handleException(Exception e) {
        logger.error(e.getMessage(), e);
        return ResultDO.error();
    }

}
