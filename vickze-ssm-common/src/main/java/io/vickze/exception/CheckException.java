package io.vickze.exception;

/**
 * 自定义异常
 *
 * @author vick.zeng
 * @email zyk@yk95.top
 * @create 2017-09-08 22:07
 **/
public class CheckException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private int code = 1;

    public CheckException(String msg) {
        super(msg);
    }

    public CheckException(String msg, Throwable e) {
        super(msg, e);
    }

    public CheckException(String msg, int code) {
        super(msg);
        this.code = code;
    }

    public CheckException(String msg, int code, Throwable e) {
        super(msg, e);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


}

