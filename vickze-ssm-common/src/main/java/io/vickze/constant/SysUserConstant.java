package io.vickze.constant;

/**
 * 系统用户常量类
 *
 * @author vick.zeng
 * @email zyk@yk95.top
 * @create 2017-09-10 0:10
 **/
public class SysUserConstant {
    /**
     * 超级管理员ID
     */
    public static final int SUPER_ADMIN = 1;

    /**
     * 账号不存在
     */
    public static final String ACCOUNT_MESSAGE = "账号不存在";

    /**
     * 密码不正确
     */
    public static final String PASSWORD_MESSAGE = "密码不正确";

    /**
     * 已登录
     */
    public static final String IN_LOGIN = "已登录";

    /**
     * 未登录
     */
    public static final String UN_LOGIN = "未登录";

    /**
     * 用户ID、昵称分隔符
     */
    public static final String USER_SPLIT = "_";

    /**
     * 状态
     */
    public enum StatusEnum {
        PAUSE(0, "账号已被锁定,请联系管理员"),
        NORMAL(1, "账号正常");

        private int status;
        private String message;

        StatusEnum(int status, String message) {
            this.status = status;
            this.message = message;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

}
