package io.vickze.dao;

import org.apache.ibatis.annotations.Mapper;

import io.vickze.entity.SysRoleDO;

/**
 * 角色
 * 
 * @author vick.zeng
 * @email zyk@yk95.top
 * @create 2017-09-23 15:53:50
 */
@Mapper
public interface SysRoleDao extends BaseDao<Long, SysRoleDO> {
	
}
