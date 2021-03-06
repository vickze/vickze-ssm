package io.vickze.service.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import io.vickze.dao.BaseDao;
import io.vickze.dao.SysRoleDao;
import io.vickze.entity.SysRoleDO;
import io.vickze.service.SysRoleMenuService;
import io.vickze.service.SysRoleService;

public class SysRoleServiceImpl extends BaseServiceImpl<Long, SysRoleDO> implements SysRoleService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SysRoleDao sysRoleDao;
    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @Override
    public SysRoleDO get(Long id) {
        SysRoleDO sysRoleDO = super.get(id);
        if (sysRoleDO == null) {
            return null;
        }

        //查询角色对应的菜单
        List<Long> menuIdList = sysRoleMenuService.listByRoleId(id);
        sysRoleDO.setMenuIdList(menuIdList);
        return sysRoleDO;
    }


    @Override
    protected BaseDao<Long, SysRoleDO> getBaseDao() {
        return sysRoleDao;
    }

    @Override
    @Transactional
    public void save(SysRoleDO sysRoleDO) {
        super.save(sysRoleDO);
        //保存角色与菜单关系
        sysRoleMenuService.saveOrUpdate(sysRoleDO.getId(), sysRoleDO.getMenuIdList());
    }

    @Override
    @Transactional
    public void saveBatch(List<SysRoleDO> sysRoleDOList) {
        super.saveBatch(sysRoleDOList);

        for (SysRoleDO sysRoleDO : sysRoleDOList) {
            //保存角色与菜单关系
            sysRoleMenuService.saveOrUpdate(sysRoleDO.getId(), sysRoleDO.getMenuIdList());
        }
    }

    @Override
    @Transactional
    public void update(SysRoleDO sysRoleDO) {
        super.update(sysRoleDO);
        //更新角色与菜单关系
        sysRoleMenuService.saveOrUpdate(sysRoleDO.getId(), sysRoleDO.getMenuIdList());
    }

    @Override
    @Transactional
    public void updateBatch(List<SysRoleDO> sysRoleDOList) {
        super.updateBatch(sysRoleDOList);

        for (SysRoleDO sysRoleDO : sysRoleDOList) {
            //保存角色与菜单关系
            sysRoleMenuService.saveOrUpdate(sysRoleDO.getId(), sysRoleDO.getMenuIdList());
        }
    }
}
