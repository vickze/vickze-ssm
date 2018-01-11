package io.vickze.service;

import io.vickze.entity.ResultDO;
import io.vickze.entity.TokenDO;
import io.vickze.entity.UpdatePasswordDO;
import io.vickze.entity.UserDO;

/**
 * @author vick.zeng
 * @email zyk@yk95.top
 * @date 2017-12-12 15:42
 */
public interface UserService extends BaseService<Long, UserDO> {

    /**
     * 用户注册
     */
    TokenDO register(UserDO userDO);

    /**
     * 用户登录
     */
    TokenDO login(UserDO userDO);

    /**
     * 用户登出
     */
    ResultDO logout(long userId);

    /**
     * 修改用户密码
     */
    ResultDO updatePassword(UserDO userDO, UpdatePasswordDO updatePasswordDO);

    /**
     * 重新获取token
     */
    TokenDO refreshToken(String token);
}
