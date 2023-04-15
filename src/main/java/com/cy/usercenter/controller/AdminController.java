package com.cy.usercenter.controller;

import com.cy.usercenter.constant.ResponseConstants;
import com.cy.usercenter.model.domain.Admin;
import com.cy.usercenter.model.domain.User;
import com.cy.usercenter.model.request.AdminBody;
import com.cy.usercenter.model.response.Response;
import com.cy.usercenter.service.AdminService;
import com.cy.usercenter.util.ExceptionUtil;
import com.cy.usercenter.util.ResponseUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private AdminService adminService;

    @RequestMapping("/login")
    public Response<Admin> login(@RequestBody AdminBody adminBody, HttpServletRequest request){
        if (adminBody==null) ExceptionUtil.throwAppErr(ResponseConstants.PARAMETER_ERROR);
        String userAccount = adminBody.getAccount();
        String password = adminBody.getPassword();
        Admin admin = adminService.login(userAccount, password, request);
        return ResponseUtil.success(admin);
    }

    @GetMapping("/search")
    public Response<List<User>> search(String username, HttpServletRequest request){
        if (!adminService.isAdmin(request)) ExceptionUtil.throwAppErr(ResponseConstants.ADMIN_AUTH_ERROR);
        List<User> users = adminService.searchUser(username);
        return ResponseUtil.success(users);
    }

    @PostMapping("/ban")
    public Response<Boolean> ban(Long id, HttpServletRequest request){
        if (adminService.isAdmin(request)) ExceptionUtil.throwAppErr(ResponseConstants.ADMIN_AUTH_ERROR);
        if (id<0) ExceptionUtil.throwAppErr(ResponseConstants.PARAMETER_ERROR);
        boolean b = adminService.banUser(id);
        return ResponseUtil.success(b);
    }

}
