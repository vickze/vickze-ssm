package io.vickze.service;

import java.util.List;

import io.vickze.entity.SysMenuDO;

/**
 * 菜单
 *
 * @author vick.zeng
 * @email 2512522383@qq.com
 * @create 2017-09-09 23:50:34
 */
public interface SysMenuService extends BaseService<Long, SysMenuDO> {

    List<SysMenuDO> listUserMenu(long userId);


    /**
     * 根据父菜单，查询子菜单
     *
     * @param parentId 父菜单ID
     */
    List<SysMenuDO> listByParentId(Long parentId);


    /**
     * 根据父菜单，查询子菜单
     *
     * @param parentId   父菜单ID
     * @param menuIdList 用户菜单ID
     */
    List<SysMenuDO> listByParentId(Long parentId, List<Long> menuIdList);

    /**
     * 获取不包含按钮的菜单列表
     *
     * @return
     */
    List<SysMenuDO> listNotButton();
}
