package com.cy.usercenter.controller;

import com.cy.usercenter.constant.ResponseConstants;
import com.cy.usercenter.model.domain.User;
import com.cy.usercenter.model.response.Response;
import com.cy.usercenter.service.AdminService;
import com.cy.usercenter.util.ExceptionUtil;
import com.cy.usercenter.util.ResponseUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasAnyAuthority('admin')")
public class AdminController {

    @Resource
    private AdminService adminService;

    @GetMapping("/search")
    public Response<List<User>> search(String username){
        List<User> users = adminService.searchUser(username);
        return ResponseUtil.success(users);
    }

    @PostMapping("/ban")
    public Response<Boolean> ban(Long id){
        if (id<0) ExceptionUtil.throwAppErr(ResponseConstants.PARAMETER_ERROR);
        boolean b = adminService.banUser(id);
        return ResponseUtil.success(b);
    }

}
