package io.vickze.entity;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import io.vickze.validator.Save;
import io.vickze.validator.Update;


/**
 * 菜单
 *
 * @author vick.zeng
 * @email zyk@yk95.top
 * @create 2017-09-09 23:50:34
 */
public class SysMenuDO extends BaseDO<Long> implements Serializable {
    private static final long serialVersionUID = 1L;

    //父菜单ID，一级菜单为0
    @NotNull(message = "上级菜单不能为空", groups = {Save.class, Update.class})
    private Long parentId;
    //父菜单名称
    private String parentName;
    //菜单名称
    @NotBlank(message = "菜单名不能为空", groups = {Save.class, Update.class})
    private String name;
    //菜单URL
    @NotBlank(message = "菜单URL不能为空", groups = {Save.class, Update.class})
    private String url;
    //授权(多个用逗号分隔，如：user:list,user:create)
    private String perms;
    //类型   0：目录   1：菜单   2：按钮
    @Range(min = 0, max = 2, message = "菜单类型有误", groups = {Save.class, Update.class})
    private Integer type;
    //菜单图标
    private String icon;
    //排序
    private Integer orderNum;

    /**
     * ztree属性
     */
    private Boolean open;

    private List<?> list;


    /**
     * 设置：父菜单ID，一级菜单为0
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * 获取：父菜单ID，一级菜单为0
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * 获取：父菜单名称
     */
    public String getParentName() {
        return parentName;
    }

    /**
     * 设置：父菜单名称
     */
    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    /**
     * 设置：菜单名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取：菜单名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置：菜单URL
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 获取：菜单URL
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置：授权(多个用逗号分隔，如：user:list,user:create)
     */
    public void setPerms(String perms) {
        this.perms = perms;
    }

    /**
     * 获取：授权(多个用逗号分隔，如：user:list,user:create)
     */
    public String getPerms() {
        return perms;
    }

    /**
     * 设置：类型   0：目录   1：菜单   2：按钮
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 获取：类型   0：目录   1：菜单   2：按钮
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置：菜单图标
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * 获取：菜单图标
     */
    public String getIcon() {
        return icon;
    }

    /**
     * 设置：排序
     */
    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    /**
     * 获取：排序
     */
    public Integer getOrderNum() {
        return orderNum;
    }

    public Boolean getOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }
}
