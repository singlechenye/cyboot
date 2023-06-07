package com.cy.cyboot.model.exception;

public class AppException extends RuntimeException{
    private final int code;
    private final String msg;
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
