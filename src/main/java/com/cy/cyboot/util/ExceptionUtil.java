package com.cy.cyboot.util;

import com.cy.cyboot.constant.ResponseConstants;
import com.cy.cyboot.model.exception.AppException;

public class ExceptionUtil {
    public static void throwAppErr(ResponseConstants responseConstants){
        int code = responseConstants.getCode();
        String msg = responseConstants.getMsg();
        throw new AppException(code,msg);
    }
}
