package io.vickze.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

import io.vickze.entity.SysUserDO;

/**
 * 系统用户
 * 
 * @author vick.zeng
 * @email zyk@yk95.top
 * @create 2017-09-09 23:50:34
 */
@Mapper
public interface SysUserDao extends BaseDao<Long, SysUserDO> {

    /**
     * 根据用户名，查询系统用户
     */
    SysUserDO getByUsername(String username);

    /**
     * 修改密码
     */
    int updatePassword(Map<String, Object> map);

    /**
     * 查询用户的所有菜单ID
     */
    List<Long> listAllMenuId(Long userId);

    /**
     * 查询用户的所有权限
     * @param userId  用户ID
     */
    List<String> listAllPerms(Long userId);
}
