package com.cy.usercenter.controller;


import com.cy.usercenter.constant.ResponseConstants;
import com.cy.usercenter.model.domain.User;
import com.cy.usercenter.model.request.LoginBody;
import com.cy.usercenter.model.request.RegisterBody;
import com.cy.usercenter.model.response.Response;
import com.cy.usercenter.util.JwtUtil;
import com.cy.usercenter.util.ResponseUtil;
import com.cy.usercenter.service.UserService;
import com.cy.usercenter.constant.UserConstants;
import com.cy.usercenter.util.ExceptionUtil;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;

import static com.cy.usercenter.util.UserUtil.getSafetyUser;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;


    @RequestMapping("/register")
    public Response<Long> register(@RequestBody RegisterBody registerBody, HttpServletRequest request){
        if(registerBody==null) ExceptionUtil.throwAppErr(ResponseConstants.PARAMETER_ERROR);
        System.out.println(registerBody);
        String userAccount = registerBody.getUserAccount();
        String password = registerBody.getPassword();
        String checkPassword = registerBody.getCheckPassword();
        long register = userService.register(userAccount, password, checkPassword);
        return ResponseUtil.success(register);

    }

    /**
     * @param loginBody
     * @param request
     * @return Response<User>
     * 有了spring security,此接口无需实现
     */
    @RequestMapping("/login")
    public Response<Object> login(@RequestBody LoginBody loginBody, HttpServletRequest request, HttpServletResponse response){
        if (loginBody==null) ExceptionUtil.throwAppErr(ResponseConstants.PARAMETER_ERROR);
        String userAccount = loginBody.getUserAccount();
        String password = loginBody.getPassword();
        String jwt = userService.login(userAccount, password);
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token",jwt);
        return ResponseUtil.success(tokenMap);
    }

    /**
     *
     * @param request
     * @return Response<Integer>
     * 有了spring security,此接口无需实现
     */
    @RequestMapping("/logout")
    @PreAuthorize("hasAnyAuthority('normal-user','vip-user')")
    public void Logout( HttpServletRequest request){
//        if (request.getAttribute(UserConstants.USER_LOGIN_STATE)==null) ExceptionUtil.throwAppErr(ResponseConstants.NOT_LOGIN_ERROR);
//        int logout = userService.Logout(request);
//        return ResponseUtil.success(logout);
    }

    @GetMapping("/current")
    @PreAuthorize("hasAnyAuthority('normal-user','vip-user')")
    public Response<User> getCurrentUser(HttpServletRequest request){
        User user = (User) request.getSession().getAttribute(UserConstants.USER_LOGIN_STATE);
        if (user==null) ExceptionUtil.throwAppErr(ResponseConstants.NOT_LOGIN_ERROR);
        User safetyUser = getSafetyUser(userService.getById(user.getId()));
        return ResponseUtil.success(safetyUser);
    }

    @PostMapping("/dump")
    @PreAuthorize("hasAnyAuthority('normal-user','vip-user')")
    public Response<Boolean> dump(HttpServletRequest request){
        User user = (User) request.getSession().getAttribute(UserConstants.USER_LOGIN_STATE);
        if (user==null) ExceptionUtil.throwAppErr(ResponseConstants.NOT_LOGIN_ERROR);
        Long id = user.getId();
        boolean b = userService.removeById(id);
        return ResponseUtil.success(b);
    }
}
