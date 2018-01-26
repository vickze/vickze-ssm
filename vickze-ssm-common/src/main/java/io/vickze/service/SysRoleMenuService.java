package io.vickze.service;


import java.util.List;

/**
 * 角色与菜单对应关系
 *
 * @author vick.zeng
 * @email 2512522383@qq.com
 * @create 2017-09-09 23:50:34
 */
public interface SysRoleMenuService {
    /**
     * 保存或者更新角色菜单关系
     *
     * @param id
     * @param menuIdList
     */
    void saveOrUpdate(Long id, List<Long> menuIdList);

    /**
     * 根据角色ID，获取菜单ID列表
     *
     * @param roleId
     * @return
     */
    List<Long> listByRoleId(Long roleId);
}
