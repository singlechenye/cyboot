package com.cy.usercenter.constant;

public enum ResponseConstants {
    SUCCESS(200000,"成功"),ADMIN_AUTH_ERROR(400001,"管理员认证失败"),
    NOT_LOGIN_ERROR(400002,"用户未登录"),PARAMETER_ERROR(400003,"参数错误"),
    REGISTER_EXIST_ERROR(400004,"注册用户已存在"),LOGIN_NOT_EXIST_ERROR(400005,"登录用户不存在"),
    SYSTEM_ERROR(400006,"系统错误"),LOGIN_PASSWORD_WRONG_ERROR(400007,"登录密码错误");

    private int code;
    private String msg;

    ResponseConstants(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
