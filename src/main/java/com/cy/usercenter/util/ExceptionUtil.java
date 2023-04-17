package com.cy.usercenter.util;

import com.cy.usercenter.constant.ResponseConstants;
import com.cy.usercenter.model.exception.AppException;

/**
 * @author 86147
 * create  12/4/2023 下午12:14
 */
public class ExceptionUtil {
    public static void throwAppErr(ResponseConstants responseConstants){
        int code = responseConstants.getCode();
        String msg = responseConstants.getMsg();
        throw new AppException(code,msg);
    }
}
