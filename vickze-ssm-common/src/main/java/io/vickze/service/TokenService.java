package io.vickze.service;

import io.vickze.entity.TokenDO;

/**
 * @author vick.zeng
 * @email zyk@yk95.top
 * @date 2017-12-12 15:09
 */
public interface TokenService {
    /**
     * 检验token，返回用户ID
     */
    long validToken(String token);

    /**
     * 生成token
     */
    TokenDO generateToken(long userId);

    /**
     * 移除token
     */
    void removeUserToken(long userId);

    /**
     * 验证token是否可刷新，返回用户ID
     */
    long validTokenCanRefresh(String token);
}
