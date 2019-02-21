package io.vickze.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 用户与角色对应关系
 * 
 * @author vick.zeng
 * @email zyk@yk95.top
 * @create 2017-09-09 23:50:34
 */
@Mapper
public interface SysUserRoleDao {

    /**
     * 根据用户ID，获取角色ID列表
     *
     * @param userId
     * @return
     */
    List<Long> listByUserId(Long userId);

    /**
     * 根据userId删除用户角色关系
     *
     * @param userId
     */
    int deleteByUserId(Long userId);

    int save(Map<String, Object> map);
}
