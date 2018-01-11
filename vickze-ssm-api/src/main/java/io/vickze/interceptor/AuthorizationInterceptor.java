package io.vickze.interceptor;

import com.alibaba.dubbo.config.annotation.Reference;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.vickze.constant.UserConstant;
import io.vickze.exception.CheckException;
import io.vickze.service.TokenService;

/**
 * 登录验证
 *
 * @author vick.zeng
 * @email zyk@yk95.top
 * @date 2017-12-11 17:58
 */
@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {
    @Reference
    private TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        Auth auth = ((HandlerMethod) handler).getMethodAnnotation(Auth.class);
        if (auth == null) {
            return true;
        }

        String token = request.getHeader("Authorization");

        if (StringUtils.isBlank(token)) {
            throw new CheckException("用户未授权", HttpStatus.UNAUTHORIZED.value());
        }

        long userId = tokenService.validToken(token);
        if (userId == UserConstant.UN_LOGIN) {
            throw new CheckException("用户未授权", HttpStatus.UNAUTHORIZED.value());
        }

        //设置userId到request里，后续根据userId，获取用户信息
        request.setAttribute(UserConstant.LOGIN_USER_KEY, userId);

        return true;
    }
}
