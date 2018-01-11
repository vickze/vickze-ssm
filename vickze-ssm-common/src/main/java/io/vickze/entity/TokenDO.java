package io.vickze.entity;

import java.io.Serializable;

/**
 * @author vick.zeng
 * @email zyk@yk95.top
 * @date 2017-12-12 15:24
 */
public class TokenDO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String token;

    private long expire;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }
}
