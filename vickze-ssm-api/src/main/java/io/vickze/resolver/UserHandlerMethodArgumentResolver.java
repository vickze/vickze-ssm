package io.vickze.resolver;

import com.alibaba.dubbo.config.annotation.Reference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import io.vickze.constant.UserConstant;
import io.vickze.entity.UserDO;
import io.vickze.service.UserService;

/**
 * @author vick.zeng
 * @email zyk@yk95.top
 * @date 2017-12-12 15:41
 */
@Component
public class UserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Reference
    private UserService userService;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().isAssignableFrom(UserDO.class)
                && methodParameter.hasParameterAnnotation(User.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        //获取用户ID
        Object object = nativeWebRequest.getAttribute(UserConstant.LOGIN_USER_KEY, RequestAttributes.SCOPE_REQUEST);
        if (object == null) {
            return null;
        }

        return userService.get((long) object);
    }
}
