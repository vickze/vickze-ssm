package io.vickze.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 用户与角色对应关系
 *
 * @author vick.zeng
 * @email 2512522383@qq.com
 * @create 2017-09-09 23:50:34
 */
public class SysUserRoleDO extends BaseDO<Long> implements Serializable {
    private static final long serialVersionUID = 1L;

    //用户ID
    private Long userId;
    //角色ID
    private Long roleId;

    /**
     * 设置：用户ID
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 获取：用户ID
     */
    public Long getUserId() {
        return userId;
    }

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

}
