package io.vickze.entity;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import io.vickze.validator.Update;

/**
 * @author vick.zeng
 * @email zengyukang@hey900.com
 * @date 2018-01-26 18:38
 */
public class BaseDO<ID extends Serializable> {

    //主键
    @NotNull(message = "请传入ID", groups = {Update.class})
    private ID id;
    //创建时间
    private Date gmtCreate;
    //最后修改时间
    private Date gmtModified;

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }
}
