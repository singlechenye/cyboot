package com.cy.usercenter.exception;

import com.cy.usercenter.model.response.Response;
import com.cy.usercenter.util.ResponseUtil;
import com.cy.usercenter.constant.ResponseConstants;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author 86147
 * create  12/4/2023 上午11:59
 */
@RestControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(AppException.class)
    public Response appExceptionHandler(AppException appException){
        System.out.println("zs"+appException);
        return ResponseUtil.err(appException.getCode(), appException.getMsg());
    }

    @ExceptionHandler(RuntimeException.class)
    public Response exceptionHandler(Exception exception){
        System.out.println("zs"+exception);
        return ResponseUtil.err(ResponseConstants.CODE_SYSTEM_ERROR, ResponseConstants.MSG_SYSTEM_ERROR);
    }

}
