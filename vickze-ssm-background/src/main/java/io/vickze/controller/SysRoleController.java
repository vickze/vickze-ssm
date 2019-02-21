package io.vickze.controller;

import com.alibaba.dubbo.config.annotation.Reference;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.vickze.constant.SysUserConstant;
import io.vickze.entity.PageDO;
import io.vickze.entity.QueryDO;
import io.vickze.entity.ResultDO;
import io.vickze.entity.SysRoleDO;
import io.vickze.service.SysRoleService;
import io.vickze.validator.Save;
import io.vickze.validator.Update;
import io.vickze.validator.ValidatorUtil;


/**
 * 角色
 *
 * @author vick.zeng
 * @email zyk@yk95.top
 * @create 2017-09-23 15:53:50
 */
@RestController
@RequestMapping("sys/role")
public class SysRoleController extends SysAbstractController {
    @Autowired
    private SysRoleService sysRoleService;

    /**
     * 列表
     */
    @GetMapping
    @RequiresPermissions("sys:role:list")
    public PageDO list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        QueryDO query = new QueryDO(params);

        List<SysRoleDO> sysRoleList = sysRoleService.list(query);
        long total = sysRoleService.count(query);

        return new PageDO(sysRoleList, total, query.getLimit(), query.getPage());
    }

    /**
     * 信息
     */
    @GetMapping("/{id}")
    @RequiresPermissions("sys:role:info")
    public SysRoleDO info(@PathVariable("id") Long id) {
        return sysRoleService.get(id);
    }

    /**
     * 保存
     */
    @PostMapping
    @RequiresPermissions("sys:role:save")
    public ResultDO save(@RequestBody @Validated({Save.class}) SysRoleDO sysRole) {
        sysRoleService.save(sysRole);

        return ResultDO.success();
    }

    /**
     * 修改
     */
    @PutMapping
    @RequiresPermissions("sys:role:update")
    public ResultDO update(@RequestBody @Validated({Update.class}) SysRoleDO sysRole) {
        sysRoleService.update(sysRole);

        return ResultDO.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    @RequiresPermissions("sys:role:delete")
    public ResultDO delete(@PathVariable("id") Long id) {
        sysRoleService.delete(id);

        return ResultDO.success();
    }


    /**
     * 批量保存
     */
    @PostMapping("/save-batch")
    @RequiresPermissions("sys:role:save")
    public ResultDO saveBatch(@RequestBody List<SysRoleDO> sysRoleDOList) {
        ValidatorUtil.validateCollection(sysRoleDOList, Save.class);
        sysRoleService.saveBatch(sysRoleDOList);

        return ResultDO.success();
    }

    /**
     * 批量修改
     */
    @PostMapping("/update-batch")
    @RequiresPermissions("sys:role:update")
    public ResultDO updateBatch(@RequestBody List<SysRoleDO> sysRoleDOList) {
        ValidatorUtil.validateCollection(sysRoleDOList, Update.class);
        sysRoleService.updateBatch(sysRoleDOList);

        return ResultDO.success();
    }

    /**
     * 批量删除
     */
    @PostMapping("/delete-batch")
    @RequiresPermissions("sys:role:delete")
    public ResultDO deleteBatch(@RequestBody Long[] ids) {
        sysRoleService.deleteBatch(ids);

        return ResultDO.success();
    }

    /**
     * 用户角色列表
     */
    @GetMapping("/select")
    @RequiresPermissions("sys:role:select")
    public List<SysRoleDO> select() {
        return sysRoleService.list();
    }
}
