package io.vickze.interceptor;


import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.vickze.constant.UserConstant;
import io.vickze.entity.ResultDO;
import io.vickze.service.TokenService;
import io.vickze.util.JsonUtil;

/**
 * 登录验证
 *
 * @author vick.zeng
 * @email zyk@yk95.top
 * @date 2017-12-11 17:58
 */
@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {
    @Autowired
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
            unauthorized(response);
            return false;
        }

        long userId = tokenService.validToken(token);
        if (userId == UserConstant.UN_LOGIN) {
            unauthorized(response);
            return false;

        }

        //设置userId到request里，后续根据userId，获取用户信息
        request.setAttribute(UserConstant.LOGIN_USER_KEY, userId);

        return true;
    }

    private void unauthorized(HttpServletResponse response) throws IOException {
        String result = JsonUtil.toJson(ResultDO.error(HttpStatus.UNAUTHORIZED.value(), "用户未授权"));

        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("application/json");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(result);
        response.getWriter().close();
    }
}
