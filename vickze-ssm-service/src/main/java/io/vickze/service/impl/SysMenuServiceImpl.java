package io.vickze.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import io.vickze.constant.SysMenuConstant;
import io.vickze.constant.SysUserConstant;
import io.vickze.dao.BaseDao;
import io.vickze.dao.SysMenuDao;
import io.vickze.entity.SysMenuDO;
import io.vickze.service.SysMenuService;
import io.vickze.service.SysUserService;

public class SysMenuServiceImpl extends BaseServiceImpl<Long, SysMenuDO> implements SysMenuService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SysMenuDao sysMenuDao;

    @Autowired
    private SysUserService sysUserService;

    @Override
    public List<SysMenuDO> listUserMenu(long userId) {
        //系统管理员，拥有最高权限
        if (userId == SysUserConstant.SUPER_ADMIN) {
            return listAllMenu(null);
        }

        //用户菜单列表
        List<Long> menuIdList = sysUserService.listAllMenuId(userId);
        return listAllMenu(menuIdList);
    }

    @Override
    public List<SysMenuDO> listByParentId(Long parentId) {
        return sysMenuDao.listByParentId(parentId);
    }

    @Override
    public List<SysMenuDO> listByParentId(Long parentId, List<Long> menuIdList) {
        List<SysMenuDO> menuList = listByParentId(parentId);
        if (menuIdList == null) {
            return menuList;
        }

        List<SysMenuDO> userMenuList = new ArrayList<>();
        for (SysMenuDO menu : menuList) {
            if (menuIdList.contains(menu.getId())) {
                userMenuList.add(menu);
            }
        }
        return userMenuList;
    }

    @Override
    public List<SysMenuDO> listNotButton() {
        //查询列表数据
        List<SysMenuDO> menuList = sysMenuDao.listNotButton();
        //添加顶级菜单
        SysMenuDO root = new SysMenuDO();
        root.setId(0L);
        root.setName("一级菜单");
        root.setParentId(-1L);
        root.setOpen(true);
        menuList.add(root);

        return menuList;
    }


    /**
     * 获取所有菜单列表
     */
    private List<SysMenuDO> listAllMenu(List<Long> menuIdList) {
        //查询根菜单列表
        List<SysMenuDO> menuList = listByParentId(0L, menuIdList);
        //递归获取子菜单
        getMenuTreeList(menuList, menuIdList);

        return menuList;
    }

    /**
     * 递归
     */
    private List<SysMenuDO> getMenuTreeList(List<SysMenuDO> menuList, List<Long> menuIdList) {
        List<SysMenuDO> subMenuList = new ArrayList<>();

        for (SysMenuDO entity : menuList) {
            if (entity.getType() == SysMenuConstant.MenuType.CATALOG.getValue()) {//目录
                entity.setList(getMenuTreeList(listByParentId(entity.getId(), menuIdList), menuIdList));
            }
            subMenuList.add(entity);
        }

        return subMenuList;
    }

    @Override
    protected BaseDao<Long, SysMenuDO> getBaseDao() {
        return sysMenuDao;
    }
}
