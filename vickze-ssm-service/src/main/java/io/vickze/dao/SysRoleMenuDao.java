package io.vickze.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 角色与菜单对应关系
 * 
 * @author vick.zeng
 * @email 2512522383@qq.com
 * @create 2017-09-09 23:50:34
 */
@Mapper
public interface SysRoleMenuDao {

    /**
     * 根据角色ID，获取菜单ID列表
     *
     * @param roleId
     * @return
     */
    List<Long> listByRoleId(Long roleId);

    /**
     * 根据角色ID，删除角色菜单关系列表
     *
     * @param roleId
     * @return
     */
    int deleteByRoleId(Long roleId);

    /**
     *
     * @param map
     */
    int save(Map<String, Object> map);
}
