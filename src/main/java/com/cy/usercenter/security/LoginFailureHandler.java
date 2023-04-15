package com.cy.usercenter.security;

import com.alibaba.fastjson.JSON;
import com.cy.usercenter.model.response.Response;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.cy.usercenter.constant.ResponseConstants.LOGIN_PASSWORD_WRONG_ERROR;
import static com.cy.usercenter.util.ExceptionUtil.throwAppErr;

/**
 * @author 徐一杰
 * @date 2022/10/25 17:19
 * security认证失败逻辑，定义这个以后，security内置的页面跳转就失效了
 */
@Component
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String msg = LOGIN_PASSWORD_WRONG_ERROR.getMsg();
        int code = LOGIN_PASSWORD_WRONG_ERROR.getCode();
        Response<Object> objectResponse = new Response<>(code, null, msg);
        String s = JSON.toJSONString(objectResponse);
        response.getWriter().write(s);
    }
}

