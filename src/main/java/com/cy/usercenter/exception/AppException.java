package com.cy.usercenter.exception;

/**
 * @author 86147
 * create  12/4/2023 上午11:58
 */
public class AppException extends RuntimeException{
    private int code;
    private String msg;
    public AppException(int code,String msg) {
        this.code=code;
        this.msg=msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
