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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

import io.vickze.entity.ResultDO;
import io.vickze.entity.SysMenuDO;
import io.vickze.service.SysMenuService;
import io.vickze.service.SysUserService;
import io.vickze.validator.Save;
import io.vickze.validator.Update;
import io.vickze.validator.ValidatorUtil;


/**
 * 菜单
 *
 * @author vick.zeng
 * @email zyk@yk95.top
 * @create 2017-09-09 23:50:34
 */
@RestController
@RequestMapping("sys/menu")
public class SysMenuController extends SysAbstractController {
    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    private SysUserService sysUserService;

    /**
     * 列表
     */
    @GetMapping
    @RequiresPermissions("sys:menu:list")
    public List<SysMenuDO> list() {
        return sysMenuService.list(null);
    }

    /**
     * 信息
     */
    @GetMapping("/{menuId}")
    @RequiresPermissions("sys:menu:info")
    public SysMenuDO info(@PathVariable("menuId") Long menuId) {
        return sysMenuService.get(menuId);
    }

    /**
     * 保存
     */
    @PostMapping
    @RequiresPermissions("sys:menu:save")
    public ResultDO save(@RequestBody @Validated({Save.class}) SysMenuDO sysMenu) {
        sysMenuService.save(sysMenu);

        return ResultDO.success();
    }

    /**
     * 修改
     */
    @PutMapping
    @RequiresPermissions("sys:menu:update")
    public ResultDO update(@RequestBody @Validated({Update.class}) SysMenuDO sysMenu) {
        sysMenuService.update(sysMenu);

        return ResultDO.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/{menuId}")
    @RequiresPermissions("sys:menu:delete")
    public ResultDO delete(@PathVariable("menuId") Long menuId) {

        //判断是否有子菜单或按钮
        List<SysMenuDO> menuList = sysMenuService.listByParentId(menuId);
        if (menuList.size() > 0) {
            return ResultDO.error("请先删除子菜单或按钮");
        }

        sysMenuService.delete(menuId);

        return ResultDO.success();
    }

    /**
     * 保存
     */
    @PostMapping("/save-batch")
    @RequiresPermissions("sys:menu:save")
    public ResultDO saveBatch(@RequestBody List<SysMenuDO> sysMenuDOList) {
        ValidatorUtil.validateCollection(sysMenuDOList, Save.class);
        sysMenuService.saveBatch(sysMenuDOList);

        return ResultDO.success();
    }

    /**
     * 修改
     */
    @PostMapping("/update-batch")
    @RequiresPermissions("sys:menu:update")
    public ResultDO updateBatch(@RequestBody List<SysMenuDO> sysMenuDOList) {
        ValidatorUtil.validateCollection(sysMenuDOList, Update.class);
        sysMenuService.updateBatch(sysMenuDOList);

        return ResultDO.success();
    }

    /**
     * 删除
     */
    @PostMapping("/delete-batch")
    @RequiresPermissions("sys:menu:delete")
    public ResultDO delete(@RequestBody Long[] menuIds) {
        for (Long menuId : menuIds) {
            //判断是否有子菜单或按钮
            List<SysMenuDO> menuList = sysMenuService.listByParentId(menuId);
            if (menuList.size() > 0) {
                return ResultDO.error("请先删除子菜单或按钮");
            }
        }
        sysMenuService.deleteBatch(menuIds);

        return ResultDO.success();
    }

    /**
     * 选择菜单(添加、修改菜单)
     */
    @RequestMapping("/select")
    @RequiresPermissions("sys:menu:select")
    public List<SysMenuDO> select() {
        return sysMenuService.listNotButton();
    }

    /**
     * 导航菜单
     */
    @GetMapping("/nav")
    public ResultDO nav() {
        long userId = getUserId();
        List<SysMenuDO> menuList = sysMenuService.listUserMenu(userId);
        Set<String> permissions = sysUserService.getPermissionsByUserId(userId);

        return ResultDO.success().put("menuList", menuList).put("permissions", permissions);
    }
}
