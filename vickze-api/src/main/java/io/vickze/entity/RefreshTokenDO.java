package io.vickze.entity;

import java.io.Serializable;

/**
 * @author vick.zeng
 * @email 2512522383@qq.com
 * @date 2017-12-14 14:39
 */
public class RefreshTokenDO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
