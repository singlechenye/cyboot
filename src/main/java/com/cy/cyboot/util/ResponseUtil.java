package com.cy.cyboot.util;

import com.cy.cyboot.model.response.Response;

import static com.cy.cyboot.constant.ResponseConstants.*;

public class ResponseUtil {

    public static <T> Response<T> success(T data){
        int code = SUCCESS.getCode();
        String msg = SUCCESS.getMsg();
        return new Response<>(code,data,msg);
    }

    public static Response error(int code,String msg ){

        return new Response<>(code,null,msg);
    }
}
