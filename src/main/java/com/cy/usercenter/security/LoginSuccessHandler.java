package com.cy.usercenter.security;

import com.alibaba.fastjson.JSON;
import com.cy.usercenter.model.response.Response;
import com.cy.usercenter.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.cy.usercenter.constant.ResponseConstants.LOGIN_PASSWORD_WRONG_ERROR;
import static com.cy.usercenter.constant.ResponseConstants.SUCCESS;

/**
 * @author 86147
 * create  14/4/2023 下午4:06
 */
@Component
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String msg = SUCCESS.getMsg();
        int code = SUCCESS.getCode();
        Response<Object> objectResponse = new Response<>(code, null, msg);
        String s = JSON.toJSONString(objectResponse);
        response.getWriter().write(s);
    }
}
