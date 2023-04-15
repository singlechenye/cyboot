package com.cy.usercenter.util;

import com.cy.usercenter.model.response.Response;

import static com.cy.usercenter.constant.ResponseConstants.*;

/**
 * @author 86147
 * create  12/4/2023 上午10:55
 */
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
