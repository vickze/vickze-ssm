package io.vickze.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 角色与菜单对应关系
 * 
 * @author vick.zeng
 * @email 2512522383@qq.com
 * @create 2017-09-09 23:50:34
 */
public class SysRoleMenuDO extends BaseDO<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	//角色ID
	private Long roleId;
	//菜单ID
	private Long menuId;

	/**
	 * 设置：角色ID
	 */
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	/**
	 * 获取：角色ID
	 */
	public Long getRoleId() {
		return roleId;
	}
	/**
	 * 设置：菜单ID
	 */
	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}
	/**
	 * 获取：菜单ID
	 */
	public Long getMenuId() {
		return menuId;
	}

}
