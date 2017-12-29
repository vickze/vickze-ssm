package io.vickze.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import io.vickze.entity.UserDO;

/**
 * @author vick.zeng
 * @email zyk@yk95.top
 * @date 2017-12-12 16:23
 */
@Mapper
public interface UserDao extends BaseDao<Long, UserDO> {

    /**
     * 根据手机号获取用户
     */
    UserDO getByMobile(String mobile);

    /**
     * 修改密码
     */
    int updatePassword(@Param("userId") long userId, @Param("oldPassword") String oldPassword,
                       @Param("newPassword") String newPassword);
}
