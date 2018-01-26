package io.vickze.controller;

import com.alibaba.dubbo.config.annotation.Reference;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.vickze.constant.SysUserConstant;
import io.vickze.entity.PageDO;
import io.vickze.entity.QueryDO;
import io.vickze.entity.ResultDO;
import io.vickze.entity.SysRoleDO;
import io.vickze.service.SysRoleService;


/**
 * 角色
 * 
 * @author vick.zeng
 * @email zyk@yk95.top
 * @create 2017-09-23 15:53:50
 */
@RestController
@RequestMapping("sys/role")
public class SysRoleController extends SysAbstractController {
	@Reference
	private SysRoleService sysRoleService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sys:role:list")
	public PageDO list(@RequestParam Map<String, Object> params){
		//查询列表数据
        QueryDO query = new QueryDO(params);

		List<SysRoleDO> sysRoleList = sysRoleService.list(query);
		long total = sysRoleService.count(query);
		
		return new PageDO(sysRoleList, total, query.getLimit(), query.getPage());
	}

	/**
	 * 角色列表
	 */
	@RequestMapping("/select")
	@RequiresPermissions("sys:role:select")
	public List<SysRoleDO> select(){
		Map<String, Object> map = new HashMap<>();

		//如果不是超级管理员，则只查询自己所拥有的角色列表
		long userId = getUserId();
		if(userId != SysUserConstant.SUPER_ADMIN){
			map.put("createUserId", userId);
		}
		return sysRoleService.list(map);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("sys:role:info")
	public SysRoleDO info(@PathVariable("id") Long id){
		return sysRoleService.get(id);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("sys:role:save")
	public ResultDO save(@RequestBody SysRoleDO sysRole){
		sysRoleService.save(sysRole);
		
		return ResultDO.success();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("sys:role:update")
	public ResultDO update(@RequestBody SysRoleDO sysRole){
		sysRoleService.update(sysRole);
		
		return ResultDO.success();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("sys:role:delete")
	public ResultDO delete(@RequestBody Long[] ids){
		sysRoleService.deleteBatch(ids);
		
		return ResultDO.success();
	}
	
}
