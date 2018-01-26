package io.vickze.controller;

import com.alibaba.dubbo.config.annotation.Reference;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import io.vickze.constant.SysUserConstant;
import io.vickze.entity.PageDO;
import io.vickze.entity.QueryDO;
import io.vickze.entity.ResultDO;
import io.vickze.entity.SysUserDO;
import io.vickze.service.SysUserService;
import io.vickze.validator.Assert;


/**
 * 系统用户
 *
 * @author vick.zeng
 * @email zyk@yk95.top
 * @create 2017-09-09 23:50:34
 */
@RestController
@RequestMapping("sys/user")
public class SysUserController extends SysAbstractController {
    @Reference
    private SysUserService sysUserService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:user:list")
    public PageDO list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        QueryDO query = new QueryDO(params);

        List<SysUserDO> sysUserList = sysUserService.list(query);
        long total = sysUserService.count(query);

        return new PageDO(sysUserList, total, query.getLimit(), query.getPage());
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{userId}")
    @RequiresPermissions("sys:user:info")
    public SysUserDO info(@PathVariable("userId") Long userId) {
        return sysUserService.get(userId);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("sys:user:save")
    public ResultDO save(@RequestBody SysUserDO sysUser) {
        sysUserService.save(sysUser);

        return ResultDO.success();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("sys:user:update")
    public ResultDO update(@RequestBody SysUserDO sysUser) {
        sysUserService.update(sysUser);

        return ResultDO.success();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sys:user:delete")
    public ResultDO delete(@RequestBody Long[] userIds) {
        sysUserService.deleteBatch(userIds);

        return ResultDO.success();
    }

    /**
     * 获取登录的用户信息
     */
    @RequestMapping("/info")
    public SysUserDO info() {
        return sysUserService.get(getUserId());
    }

    @RequestMapping("/password")
    public ResultDO password(String password, String newPassword) {
        Assert.isBlank(password, "原密码不为能空");
        Assert.isBlank(newPassword, "新密码不为能空");

        SysUserDO sysUserDO = sysUserService.get(getUserId());
        //sha256加密
        password = new Sha256Hash(password, sysUserDO.getSalt()).toHex();
        //sha256加密
        newPassword = new Sha256Hash(newPassword, sysUserDO.getSalt()).toHex();

        //更新密码
        int count = sysUserService.updatePassword(getUserId(), password, newPassword);

        if (count == 0) {
            return ResultDO.error("原密码不正确");
        }

        //shiro登出
        SecurityUtils.getSubject().logout();

        return ResultDO.success();
    }

}
