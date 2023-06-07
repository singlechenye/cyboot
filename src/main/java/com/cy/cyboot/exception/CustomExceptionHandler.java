package com.cy.cyboot.exception;

import com.cy.cyboot.constant.ResponseConstants;
import com.cy.cyboot.model.exception.AppException;
import com.cy.cyboot.model.response.Response;
import com.cy.cyboot.util.ResponseUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.security.sasl.AuthenticationException;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(AppException.class)
    public Response appExceptionHandler(AppException appException){
        appException.printStackTrace();
        int code = appException.getCode();
        String msg = appException.getMsg();
        return ResponseUtil.error(code,msg);
    }

    @ExceptionHandler(AuthenticationException.class)
    public Response authExceptionHandler(Exception exception){
        exception.printStackTrace();
        int code = ResponseConstants.AUTH_CHECK_ERROR.getCode();
        String msg = ResponseConstants.AUTH_CHECK_ERROR.getMsg();
        return ResponseUtil.error(code,msg);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public Response accessExceptionHandler(Exception exception){
        exception.printStackTrace();
        int code = ResponseConstants.AUTH_LACK_ERROR.getCode();
        String msg = ResponseConstants.AUTH_LACK_ERROR.getMsg();
        return ResponseUtil.error(code,msg);
    }
}
