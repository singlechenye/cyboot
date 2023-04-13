package com.cy.usercenter.util;

import com.cy.usercenter.exception.AppException;

/**
 * @author 86147
 * create  12/4/2023 下午12:14
 */
public class ExceptionUtil {
    public static void throwAppErr(int code,String msg){
        throw new AppException(code,msg);
    }
}
