package io.vickze.service;

import java.util.List;

/**
 * 用户与角色对应关系
 *
 * @author vick.zeng
 * @email zyk@yk95.top
 * @create 2017-09-09 23:50:34
 */
public interface SysUserRoleService {

    /**
     * 保存用户角色关系
     *
     * @param userId
     * @param roleIdList
     */
    void saveOrUpdate(Long userId, List<Long> roleIdList);


    /**
     * 根据userId删除用户角色关系
     *
     * @param userId
     */
    void deleteByUserId(Long userId);

    /**
     * 根据用户ID，获取角色ID列表
     *
     * @param userId
     * @return
     */
    List<Long> listByUserId(Long userId);
}
