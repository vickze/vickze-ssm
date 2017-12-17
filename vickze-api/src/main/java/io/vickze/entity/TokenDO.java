package io.vickze.entity;

import java.io.Serializable;

/**
 * @author vick.zeng
 * @email 2512522383@qq.com
 * @date 2017-12-12 15:24
 */
public class TokenDO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String token;

    private long tokenExpire;

    //过期时间为30天，当token过期后可以通过refreshToken重新获取token、refreshToken，当用户超过30天未使用app，则不会自动登录
    private String refreshToken;

    private long refreshTokenExpire;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getTokenExpire() {
        return tokenExpire;
    }

    public void setTokenExpire(long tokenExpire) {
        this.tokenExpire = tokenExpire;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public long getRefreshTokenExpire() {
        return refreshTokenExpire;
    }

    public void setRefreshTokenExpire(long refreshTokenExpire) {
        this.refreshTokenExpire = refreshTokenExpire;
    }
}
