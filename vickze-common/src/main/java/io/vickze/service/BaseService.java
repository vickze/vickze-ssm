package io.vickze.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import io.vickze.dao.BaseDao;

/**
 * @author vick.zeng
 * @email 2512522383@qq.com
 * @date 2017-12-07 17:06
 */
public interface BaseService<ID extends Serializable, T> {

    BaseDao<ID, T> getBaseDao();

    default void save(T t) {
        getBaseDao().save(t);
    }

    default void saveBatch(List<T> list) {
        getBaseDao().saveBatch(list);
    }

    default void update(T t) {
        getBaseDao().update(t);
    }

    default void delete(ID id) {
        getBaseDao().delete(id);
    }

    default void deleteBatch(ID[] id) {
        getBaseDao().deleteBatch(id);
    }

    default T get(ID id) {
        return getBaseDao().get(id);
    }

    default List<T> list(Map<String, Object> map) {
        return getBaseDao().list(map);
    }

    default List<T> list() {
        return getBaseDao().list();
    }

    default long count(Map<String, Object> map) {
        return getBaseDao().count(map);
    }

    default long count() {
        return getBaseDao().count();
    }
}
