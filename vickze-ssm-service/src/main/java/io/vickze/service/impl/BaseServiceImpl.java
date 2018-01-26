package io.vickze.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import io.vickze.aspect.Sync;
import io.vickze.dao.BaseDao;
import io.vickze.entity.BaseDO;

/**
 * @author vick.zeng
 * @email zengyukang@hey900.com
 * @date 2018-01-26 18:31
 */
public abstract class BaseServiceImpl<ID extends Serializable, T extends BaseDO<ID>> {
    protected abstract BaseDao<ID, T> getBaseDao();

    @Sync
    public void save(T t) {
        Date now = new Date();
        t.setGmtCreate(now);
        t.setGmtModified(now);
        getBaseDao().save(t);
    }

    public void saveBatch(List<T> list) {
        Date now = new Date();
        for (T t : list) {
            t.setGmtCreate(now);
            t.setGmtModified(now);
            getBaseDao().save(t);
            getBaseDao().saveBatch(list);
        }
    }

    public void update(T t) {
        Date now = new Date();
        t.setGmtModified(now);
        getBaseDao().update(t);
    }

    public void delete(ID id) {
        getBaseDao().delete(id);
    }

    public void deleteBatch(ID[] id) {
        getBaseDao().deleteBatch(id);
    }

    public T get(ID id) {
        return getBaseDao().get(id);
    }

    public List<T> list(Map<String, Object> map) {
        return getBaseDao().list(map);
    }

    public List<T> list() {
        return getBaseDao().list();
    }

    public long count(Map<String, Object> map) {
        return getBaseDao().count(map);
    }

    public long count() {
        return getBaseDao().count();
    }
}
