package io.vickze.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import io.vickze.validator.Phone;
import io.vickze.validator.Register;
import io.vickze.validator.Save;
import io.vickze.validator.Update;


/**
 * 系统用户
 * 
 * @author vick.zeng
 * @email zyk@yk95.top
 * @create 2017-09-09 23:50:34
 */
public class SysUserDO extends BaseDO<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	//用户名
	@NotBlank(message = "用户名不能为空", groups = {Save.class, Update.class})
	private String username;
	//密码
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@NotBlank(message = "密码不能为空", groups = {Save.class})
	private String password;
	//盐
	@JsonIgnore
	private String salt;
	//邮箱
	@Email(message = "邮箱格式错误", groups = {Save.class, Update.class})
	private String email;
	//手机号
	@Phone(groups = {Save.class, Update.class})
	private String mobile;
	//状态  0：禁用   1：正常
	private Integer status;
	//角色ID列表
	private List<Long> roleIdList;

	/**
	 * 设置：用户名
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * 获取：用户名
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * 设置：密码
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * 获取：密码
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * 设置：盐
	 */
	public void setSalt(String salt) {
		this.salt = salt;
	}
	/**
	 * 获取：盐
	 */
	public String getSalt() {
		return salt;
	}
	/**
	 * 设置：邮箱
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * 获取：邮箱
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * 设置：手机号
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	/**
	 * 获取：手机号
	 */
	public String getMobile() {
		return mobile;
	}
	/**
	 * 设置：状态  0：禁用   1：正常
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：状态  0：禁用   1：正常
	 */
	public Integer getStatus() {
		return status;
	}

	public List<Long> getRoleIdList() {
		return roleIdList;
	}

	public void setRoleIdList(List<Long> roleIdList) {
		this.roleIdList = roleIdList;
	}
}
