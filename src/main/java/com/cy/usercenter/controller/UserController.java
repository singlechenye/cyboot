package com.cy.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cy.usercenter.model.domain.User;
import com.cy.usercenter.model.request.LoginBody;
import com.cy.usercenter.model.request.RegisterBody;
import com.cy.usercenter.model.response.Response;
import com.cy.usercenter.util.ResponseUtil;
import com.cy.usercenter.service.UserService;
import com.cy.usercenter.constant.ResponseConstants;
import com.cy.usercenter.constant.UserConstants;
import com.cy.usercenter.util.ExceptionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @RequestMapping("/register")
    public Response<Long> register(@RequestBody RegisterBody registerBody, HttpServletRequest request){
        if(registerBody==null) ExceptionUtil.throwAppErr(ResponseConstants.CODE_PARAMETER_ERROR, ResponseConstants.MSG_PARAMETER_ERROR);
        System.out.println(registerBody);
        String userAccount = registerBody.getUserAccount();
        String password = registerBody.getPassword();
        String checkPassword = registerBody.getCheckPassword();
        long register = userService.register(userAccount, password, checkPassword);
        return ResponseUtil.success(register);

    }
    @RequestMapping("/login")
    public Response<User> login(@RequestBody LoginBody loginBody, HttpServletRequest request){
        if (loginBody==null) ExceptionUtil.throwAppErr(ResponseConstants.CODE_PARAMETER_ERROR, ResponseConstants.MSG_PARAMETER_ERROR);
        String userAccount = loginBody.getUserAccount();
        String password = loginBody.getPassword();
        User user = userService.login(userAccount, password, request);
        return ResponseUtil.success(user);
    }
    @RequestMapping("/logout")
    public Response<Integer> Logout( HttpServletRequest request){
        if (request.getAttribute(UserConstants.USER_LOGIN_STATE)==null) ExceptionUtil.throwAppErr(ResponseConstants.CODE_NOT_LOGIN_ERROR, ResponseConstants.MSG_NOT_LOGIN_ERROR);
        int logout = userService.Logout(request);
        return ResponseUtil.success(logout);
    }
    @GetMapping("/search")
    public Response<List<User>> search(String username, HttpServletRequest request){
        if (!isAdmin(request)) ExceptionUtil.throwAppErr(ResponseConstants.CODE_AUTH_ERROR, ResponseConstants.MSG_AUTH_ERROR);
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(username)) userQueryWrapper.like("username",username);
        List<User> users = userService.list(userQueryWrapper).stream().map(user -> {
            userService.getSafetyUser(user);
            return user;
        }).collect(Collectors.toList());
        return ResponseUtil.success(users);
    }
    @GetMapping("/current")
    public Response<User> getCurrentUser(HttpServletRequest request){
        User user = (User) request.getSession().getAttribute(UserConstants.USER_LOGIN_STATE);
        if (user==null) ExceptionUtil.throwAppErr(ResponseConstants.CODE_NOT_LOGIN_ERROR, ResponseConstants.MSG_NOT_LOGIN_ERROR);
        User safetyUser = userService.getSafetyUser(userService.getById(user.getId()));
        return ResponseUtil.success(safetyUser);
    }
    @PostMapping("/delete")
    public Response<Boolean> deleteById(Long id, HttpServletRequest request){
        if (isAdmin(request)) ExceptionUtil.throwAppErr(ResponseConstants.CODE_AUTH_ERROR, ResponseConstants.MSG_AUTH_ERROR);
        if (id<0) ExceptionUtil.throwAppErr(ResponseConstants.CODE_PARAMETER_ERROR, ResponseConstants.MSG_PARAMETER_ERROR);
        boolean b = userService.removeById(id);
        return ResponseUtil.success(b);
    }
    private boolean isAdmin(HttpServletRequest request) {
        User user;
        if ((user = (User) request.getSession().getAttribute(UserConstants.USER_LOGIN_STATE))==null) ExceptionUtil.throwAppErr(ResponseConstants.CODE_NOT_LOGIN_ERROR, ResponseConstants.MSG_NOT_LOGIN_ERROR);
        return user.getUserRole() == UserConstants.USER_ROLE_ADMIN;
    }

}
