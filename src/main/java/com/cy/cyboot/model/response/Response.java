package com.cy.cyboot.model.response;

import lombok.Data;

import java.io.Serializable;

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
