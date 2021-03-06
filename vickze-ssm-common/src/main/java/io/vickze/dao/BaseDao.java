package io.vickze.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 基础Dao(还需在XML文件里，有对应的SQL语句)
 *
 * @author vick.zeng
 * @email zyk@yk95.top
 * @create 2017-09-08 22:07
 */
public interface BaseDao<ID extends Serializable, T> {

    int save(T t);

    int saveBatch(List<T> list);

    int update(T t);

    int updateBatch(List<T> list);

    int delete(ID id);

    int deleteBatch(ID[] id);

    T get(ID id);

    List<T> list(Map<String, Object> map);

    List<T> list();

    long count(Map<String, Object> map);

    long count();
}
