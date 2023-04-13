package com.cy.usercenter.model.response;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 86147
 * create  12/4/2023 上午10:43
 */
@Data
public class Response<T> implements Serializable {
    private int code;

    private T data;

    private String msg;

    public Response(int code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }
}
