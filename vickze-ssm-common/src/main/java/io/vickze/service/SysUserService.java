package io.vickze.service;

import java.util.List;
import java.util.Set;

import io.vickze.entity.ResultDO;
import io.vickze.entity.SysUserDO;

/**
 * 系统用户
 *
 * @author vick.zeng
 * @email zyk@yk95.top
 * @create 2017-09-09 23:50:34
 */
public interface SysUserService extends BaseService<Long, SysUserDO> {

    /**
     * 根据用户名获取用户
     */
    SysUserDO getByUsername(String username);

    /**
     * 获取用户权限列表
     */
    Set<String> getPermissionsByUserId(long userId);

    /**
     * 查询用户的所有菜单ID
     */
    List<Long> listAllMenuId(Long userId);

    /**
     * 修改密码
     */
    int updatePassword(Long userId, String password, String newPassword);
}
