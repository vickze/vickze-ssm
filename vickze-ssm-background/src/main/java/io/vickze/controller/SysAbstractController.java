package io.vickze.controller;

import com.alibaba.dubbo.config.annotation.Reference;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.vickze.constant.SysUserConstant;
import io.vickze.entity.SysUserDO;
import io.vickze.service.SysUserService;

/**
 * Controller公共组件
 *
 * @author vick.zeng
 * @email 2512522383@qq.com
 * @create 2017-09-08 22:07
 */
public abstract class SysAbstractController {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected Long getUserId() {
        String principal = SecurityUtils.getSubject().getPrincipal().toString();
        if (principal != null) {
            return Long.valueOf(principal.split(SysUserConstant.USER_SPLIT)[0]);
        }
        return -1L;
    }

    protected String getUsername() {
        String principal = SecurityUtils.getSubject().getPrincipal().toString();
        if (principal != null) {
            return principal.split(SysUserConstant.USER_SPLIT)[1];
        }
        return null;
    }

}
