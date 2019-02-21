package io.vickze.service.impl;

import com.alibaba.dubbo.config.annotation.Service;

import io.vickze.aspect.Sync;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.vickze.constant.SysUserConstant;
import io.vickze.dao.BaseDao;
import io.vickze.dao.SysUserDao;
import io.vickze.entity.SysMenuDO;
import io.vickze.entity.SysUserDO;
import io.vickze.service.SysMenuService;
import io.vickze.service.SysUserRoleService;
import io.vickze.service.SysUserService;

public class SysUserServiceImpl extends BaseServiceImpl<Long, SysUserDO> implements SysUserService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SysUserDao sysUserDao;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Autowired
    private SysMenuService sysMenuService;

    @Override
    public SysUserDO get(Long userId) {
        SysUserDO sysUserDO = sysUserDao.get(userId);
        if (sysUserDO == null) {
            return null;
        }

        //获取用户所属的角色列表
        List<Long> roleIdList = sysUserRoleService.listByUserId(userId);
        sysUserDO.setRoleIdList(roleIdList);

        return sysUserDO;
    }

    @Override
    protected BaseDao<Long, SysUserDO> getBaseDao() {
        return sysUserDao;
    }

    @Override
    @Transactional
    public void save(SysUserDO sysUserDO) {
        //sha256加密
        String salt = RandomStringUtils.randomAlphanumeric(20);
        sysUserDO.setPassword(new Sha256Hash(sysUserDO.getPassword(), salt).toHex());
        sysUserDO.setSalt(salt);
        super.save(sysUserDO);

        sysUserRoleService.saveOrUpdate(sysUserDO.getId(), sysUserDO.getRoleIdList());
    }

    @Override
    @Transactional
    public void saveBatch(List<SysUserDO> sysUserDOList) {
        for (SysUserDO sysUserDO : sysUserDOList) {
            //sha256加密
            String salt = RandomStringUtils.randomAlphanumeric(20);
            sysUserDO.setPassword(new Sha256Hash(sysUserDO.getPassword(), salt).toHex());
            sysUserDO.setSalt(salt);
        }
        super.saveBatch(sysUserDOList);

        for (SysUserDO sysUserDO : sysUserDOList) {
            sysUserRoleService.saveOrUpdate(sysUserDO.getId(), sysUserDO.getRoleIdList());
        }
    }

    @Override
    @Transactional
    public void update(SysUserDO sysUserDO) {
        super.update(sysUserDO);

        sysUserRoleService.saveOrUpdate(sysUserDO.getId(), sysUserDO.getRoleIdList());
    }

    @Override
    @Transactional
    public void updateBatch(List<SysUserDO> sysUserDOList) {
        super.updateBatch(sysUserDOList);

        for (SysUserDO sysUserDO : sysUserDOList) {
            sysUserRoleService.saveOrUpdate(sysUserDO.getId(), sysUserDO.getRoleIdList());
        }
    }

    @Override
    public SysUserDO getByUsername(String username) {
        return sysUserDao.getByUsername(username);
    }

    @Override
    public Set<String> getPermissionsByUserId(long userId) {
        List<String> permsList;

        //系统管理员，拥有最高权限
        if (userId == SysUserConstant.SUPER_ADMIN) {
            List<SysMenuDO> menuList = sysMenuService.list(new HashMap<>());
            permsList = new ArrayList<>(menuList.size());
            for (SysMenuDO menu : menuList) {
                permsList.add(menu.getPerms());
            }
        } else {
            permsList = sysUserDao.listAllPerms(userId);
        }
        //用户权限列表
        Set<String> permsSet = new HashSet<>();
        for (String perms : permsList) {
            if (StringUtils.isBlank(perms)) {
                continue;
            }
            permsSet.addAll(Arrays.asList(perms.trim().split(",")));
        }
        return permsSet;
    }

    @Override
    public List<Long> listAllMenuId(Long userId) {
        return sysUserDao.listAllMenuId(userId);
    }

    @Override
    public int updatePassword(Long userId, String password, String newPassword) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("password", password);
        map.put("newPassword", newPassword);
        map.put("gmtModified", new Date());
        return sysUserDao.updatePassword(map);
    }
}
