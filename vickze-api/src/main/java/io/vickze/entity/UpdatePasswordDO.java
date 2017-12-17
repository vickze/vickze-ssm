package io.vickze.entity;

import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * @author vick.zeng
 * @email 2512522383@qq.com
 * @date 2017-12-13 17:38
 */
public class UpdatePasswordDO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;

    @NotBlank(message = "新密码不能为空")
    private String newPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
