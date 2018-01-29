package io.vickze.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import io.vickze.entity.PageDO;
import io.vickze.entity.QueryDO;
import io.vickze.entity.ResultDO;
import io.vickze.entity.SysUserDO;
import io.vickze.service.SysUserService;
import io.vickze.validator.Assert;
import io.vickze.validator.Save;
import io.vickze.validator.Update;
import io.vickze.validator.ValidatorUtil;


/**
 * 系统用户
 *
 * @author vick.zeng
 * @email zyk@yk95.top
 * @create 2017-09-09 23:50:34
 */
@RestController
@RequestMapping(value = "sys/user", produces = "application/json")
public class SysUserController extends SysAbstractController {
    @Autowired
    private SysUserService sysUserService;

    /**
     * 列表
     */
    @GetMapping
    @RequiresPermissions("sys:user:list")
    @ResponseBody
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
    @GetMapping("/{userId}")
    @RequiresPermissions("sys:user:info")
    public SysUserDO info(@PathVariable("userId") Long userId) {
        return sysUserService.get(userId);
    }

    /**
     * 保存
     */
    @PostMapping
    @RequiresPermissions("sys:user:save")
    public ResultDO save(@RequestBody @Validated({Save.class}) SysUserDO sysUser) {
        sysUserService.save(sysUser);

        return ResultDO.success();
    }

    /**
     * 修改
     */
    @PutMapping
    @RequiresPermissions("sys:user:update")
    public ResultDO update(@RequestBody @Validated({Update.class}) SysUserDO sysUser) {
        sysUserService.update(sysUser);

        return ResultDO.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/{userId}")
    @RequiresPermissions("sys:user:delete")
    public ResultDO delete(@PathVariable("userId") Long userId) {
        sysUserService.delete(userId);

        return ResultDO.success();
    }

    /**
     * 批量保存
     */
    @PostMapping("/save-batch")
    @RequiresPermissions("sys:user:delete")
    public ResultDO saveBatch(@RequestBody List<SysUserDO> sysUserDOList) {
        ValidatorUtil.validateCollection(sysUserDOList, Save.class);
        sysUserService.saveBatch(sysUserDOList);

        return ResultDO.success();
    }

    /**
     * 批量更新
     */
    @PostMapping("/update-batch")
    @RequiresPermissions("sys:user:delete")
    public ResultDO updateBatch(@RequestBody List<SysUserDO> sysUserDOList) {
        ValidatorUtil.validateCollection(sysUserDOList, Update.class);
        sysUserService.updateBatch(sysUserDOList);

        return ResultDO.success();
    }

    /**
     * 批量删除
     */
    @PostMapping("/delete-batch")
    @RequiresPermissions("sys:user:delete")
    public ResultDO deleteBatch(@RequestBody Long[] userIds) {
        sysUserService.deleteBatch(userIds);

        return ResultDO.success();
    }

    /**
     * 获取登录的用户信息
     */
    @GetMapping("/info")
    public SysUserDO info() {
        return sysUserService.get(getUserId());
    }

    /**
     * 修改密码
     */
    @PostMapping("/update-password")
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
