package io.vickze.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import io.vickze.dao.BaseDao;
import io.vickze.entity.BaseDO;

/**
 * @author vick.zeng
 * @email zyk@yk95.top
 * @date 2017-12-07 17:06
 */
public interface BaseService<ID extends Serializable, T extends BaseDO> {

    void save(T t);

    void saveBatch(List<T> list);

    void update(T t);

    void updateBatch(List<T> list);

    void delete(ID id);

    void deleteBatch(ID[] id);

    T get(ID id);

    List<T> list(Map<String, Object> map);

    List<T> list();

    long count(Map<String, Object> map);

    long count();
}
