package io.vickze.controller;

import com.alibaba.dubbo.config.annotation.Reference;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.vickze.constant.UserConstant;
import io.vickze.entity.RefreshTokenDO;
import io.vickze.entity.ResultDO;
import io.vickze.entity.TokenDO;
import io.vickze.entity.UpdatePasswordDO;
import io.vickze.entity.UserDO;
import io.vickze.interceptor.Auth;
import io.vickze.resolver.User;
import io.vickze.service.UserService;
import io.vickze.validator.Login;
import io.vickze.validator.Register;

/**
 * @author vick.zeng
 * @email zyk@yk95.top
 * @create 2017-09-10 0:17
 */
@RestController
@RequestMapping("/api/user")
public class ApiUserController {
    @Reference(timeout = 5000)
    private UserService userService;

    @PostMapping("/register")
    public TokenDO register(@RequestBody @Validated({Register.class}) UserDO userDO) {
        return userService.register(userDO);
    }

    @PostMapping("/login")
    public TokenDO login(@RequestBody @Validated({Login.class}) UserDO userDO) {
        return userService.login(userDO);
    }

    @Auth
    @PostMapping("/logout")
    public ResultDO logout(@RequestAttribute(UserConstant.LOGIN_USER_KEY) long userId) {
        return userService.logout(userId);
    }


    @Auth
    @GetMapping("/get")
    public UserDO get(@User UserDO userDO) {
        return userDO;
    }

    @Auth
    @PostMapping("/update_password")
    public ResultDO updatePassword(@User UserDO userDO, @RequestBody @Validated UpdatePasswordDO updatePasswordDO) {
        return userService.updatePassword(userDO, updatePasswordDO);
    }

    @GetMapping("/refresh_token")
    public TokenDO refreshToken(@Validated @NotBlank @RequestHeader(value = "Authorization", defaultValue = "") String token) {
        return userService.refreshToken(token);
    }
}
