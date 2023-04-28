package com.cy.usercenter.controller;

import com.cy.usercenter.constant.ResponseConstants;
import com.cy.usercenter.model.domain.User;
import com.cy.usercenter.model.request.LoginBody;
import com.cy.usercenter.model.request.RegisterBody;
import com.cy.usercenter.model.response.Response;
import com.cy.usercenter.util.ResponseUtil;
import com.cy.usercenter.service.UserService;
import com.cy.usercenter.util.ExceptionUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 包括user的登录注册以及登出还有修改信息的接口
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;




    @RequestMapping("/register")
    public Response<Object> register(@RequestBody RegisterBody registerBody){
        if(Objects.isNull(registerBody)) ExceptionUtil.throwAppErr(ResponseConstants.PARAMETER_ERROR);
        String username = registerBody.getUsername();
        String password = registerBody.getPassword();
        String checkPassword = registerBody.getCheckPassword();
        userService.register(username, password, checkPassword);
        return ResponseUtil.success(null);
    }

    /**
     * 登录成功后会返回一个token,实现单点登录
     * @return Response<User>
     */
    @RequestMapping("/login")
    public Response<Object> login(@RequestBody LoginBody loginBody){
        if (Objects.isNull(loginBody)) ExceptionUtil.throwAppErr(ResponseConstants.PARAMETER_ERROR);
        String username = loginBody.getUsername();
        String password = loginBody.getPassword();
        String jwt = userService.login(username, password);
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token",jwt);
        return ResponseUtil.success(tokenMap);
    }


    @RequestMapping("/info")
    @PreAuthorize("hasAnyAuthority('normal','admin')")
    public Response<User> Info( HttpServletRequest request){
        User user = userService.Info();
        return ResponseUtil.success(user);
    }
    /**
     *登出账号
     * @param request
     * @return Response<Integer>
     */
    @RequestMapping("/logout")
    @PreAuthorize("hasAnyAuthority('normal','admin')")
    public Response<Object> Logout( HttpServletRequest request){
        userService.Logout();
        return ResponseUtil.success(null);
    }

    /**
     * 这个是直接删除账号,注销
     * @return
     */
    @PostMapping("/dump")
    @PreAuthorize("hasAnyAuthority('normal','admin')")
    public Response<Object> dump(){
        userService.dump();
        return ResponseUtil.success(null);
    }
}
