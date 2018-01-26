package io.vickze.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 角色
 * 
 * @author vick.zeng
 * @email 2512522383@qq.com
 * @create 2017-09-23 15:53:50
 */
public class SysRoleDO extends BaseDO<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	//角色名称
	private String roleName;
	//备注
	private String remark;

	private List<Long> menuIdList;

	/**
	 * 设置：角色名称
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	/**
	 * 获取：角色名称
	 */
	public String getRoleName() {
		return roleName;
	}
	/**
	 * 设置：备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * 获取：备注
	 */
	public String getRemark() {
		return remark;
	}

	public List<Long> getMenuIdList() {
		return menuIdList;
	}

	public void setMenuIdList(List<Long> menuIdList) {
		this.menuIdList = menuIdList;
	}
}
