package io.vickze.service.impl;

import com.alibaba.dubbo.config.annotation.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.vickze.dao.SysUserRoleDao;
import io.vickze.service.SysUserRoleService;

@Service(interfaceClass = SysUserRoleService.class, timeout = 5000)
public class SysUserRoleServiceImpl implements SysUserRoleService {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private SysUserRoleDao sysUserRoleDao;

	@Override
	public void saveOrUpdate(Long userId, List<Long> roleIdList) {
		//先删除用户与角色关系
		sysUserRoleDao.deleteByUserId(userId);

		if(roleIdList.size() == 0){
			return;
		}

		//保存用户与角色关系
		Map<String, Object> map = new HashMap<>();
		map.put("userId", userId);
		map.put("roleIdList", roleIdList);
		sysUserRoleDao.save(map);
	}

	@Override
	public void deleteByUserId(Long userId){
		sysUserRoleDao.deleteByUserId(userId);
	}



    @Override
    public List<Long> listByUserId(Long userId) {
		return sysUserRoleDao.listByUserId(userId);
    }

}
