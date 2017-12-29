package io.vickze.service;

import io.vickze.entity.TokenDO;

/**
 * @author vick.zeng
 * @email zyk@yk95.top
 * @date 2017-12-12 15:09
 */
public interface TokenService {
    /**
     * 根据token获取用户ID
     */
    long getUserIdByToken(String token);

    /**
     * 生成token
     */
    TokenDO generateToken(long userId);

    /**
     * 移除token
     */
    void removeUserToken(long userId);
}
