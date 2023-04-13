package com.cy.usercenter.util;

import com.cy.usercenter.model.response.Response;

import static com.cy.usercenter.constant.ResponseConstants.*;

/**
 * @author 86147
 * create  12/4/2023 上午10:55
 */
public class ResponseUtil {

    public static <T> Response<T> success(T data){
        return new Response<>(CODE_SUCCESS,data,MSG_SUCCESS);
    }

    public static Response err(int code,String message){
        return new Response<>(code,null,message);
    }
}
