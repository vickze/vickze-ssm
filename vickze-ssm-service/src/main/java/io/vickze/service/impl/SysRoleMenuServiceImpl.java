package io.vickze.service.impl;

import com.alibaba.dubbo.config.annotation.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.vickze.dao.SysRoleMenuDao;
import io.vickze.service.SysRoleMenuService;

@Service(interfaceClass = SysRoleMenuService.class, timeout = 5000)
public class SysRoleMenuServiceImpl implements SysRoleMenuService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SysRoleMenuDao sysRoleMenuDao;

    @Override
    public void saveOrUpdate(Long roleId, List<Long> menuIdList) {
        //先删除角色与菜单关系
        sysRoleMenuDao.deleteByRoleId(roleId);

        if (menuIdList.size() == 0) {
            return;
        }

        //保存角色与菜单关系
        Map<String, Object> map = new HashMap<>();
        map.put("roleId", roleId);
        map.put("menuIdList", menuIdList);
        sysRoleMenuDao.save(map);
    }

    @Override
    public List<Long> listByRoleId(Long roleId) {
        return sysRoleMenuDao.listByRoleId(roleId);
    }

}
