package io.vickze.controller;

import com.alibaba.dubbo.config.annotation.Reference;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

import io.vickze.entity.ResultDO;
import io.vickze.entity.SysMenuDO;
import io.vickze.service.SysMenuService;
import io.vickze.service.SysUserService;


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
    @Reference
    private SysMenuService sysMenuService;
    @Reference
    private SysUserService sysUserService;


    /**
     * 导航菜单
     */
    @RequestMapping("/nav")
    public ResultDO nav() {
        long userId = getUserId();
        List<SysMenuDO> menuList = sysMenuService.listUserMenu(userId);
        Set<String> permissions = sysUserService.getPermissionsByUserId(userId);

        return ResultDO.success().put("menuList", menuList).put("permissions", permissions);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:menu:list")
    public List<SysMenuDO> list() {
        return sysMenuService.list(null);
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
     * 信息
     */
    @RequestMapping("/info/{menuId}")
    @RequiresPermissions("sys:menu:info")
    public SysMenuDO info(@PathVariable("menuId") Long menuId) {
        return sysMenuService.get(menuId);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("sys:menu:save")
    public ResultDO save(@RequestBody SysMenuDO sysMenu) {
        sysMenuService.save(sysMenu);

        return ResultDO.success();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("sys:menu:update")
    public ResultDO update(@RequestBody SysMenuDO sysMenu) {
        sysMenuService.update(sysMenu);

        return ResultDO.success();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sys:menu:delete")
    public ResultDO delete(long menuId) {

        //判断是否有子菜单或按钮
        List<SysMenuDO> menuList = sysMenuService.listByParentId(menuId);
        if (menuList.size() > 0) {
            return ResultDO.error("请先删除子菜单或按钮");
        }

        sysMenuService.deleteBatch(new Long[]{menuId});

        return ResultDO.success();
    }
}
