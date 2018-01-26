package io.vickze.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;

import io.vickze.validator.Login;
import io.vickze.validator.Phone;
import io.vickze.validator.Register;

/**
 * @author vick.zeng
 * @email zyk@yk95.top
 * @date 2017-12-12 15:24
 */
public class UserDO implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id;

    @NotBlank(message = "昵称不能为空", groups = {Register.class})
    private String nickname;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "密码不能为空", groups = {Register.class, Login.class})
    private String password;

    @JsonIgnore
    private String salt;

    @Phone(groups = {Register.class, Login.class})
    private String mobile;
    //创建时间
    private Date createTime;
    //最后修改时间
    private Date lastModifiedTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Date getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(Date lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }
}
