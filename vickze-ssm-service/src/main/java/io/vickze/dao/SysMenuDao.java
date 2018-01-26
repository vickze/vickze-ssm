package io.vickze.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

import io.vickze.entity.SysMenuDO;

/**
 * 菜单
 * 
 * @author vick.zeng
 * @email zyk@yk95.top
 * @create 2017-09-09 23:50:34
 */
@Mapper
public interface SysMenuDao extends BaseDao<Long, SysMenuDO> {

    List<SysMenuDO> listByParentId(Long parentId);

    List<SysMenuDO> listNotButton();
}
