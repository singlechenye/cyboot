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
import java.util.List;

/**
 * 当用户登录后权限为管理员时可以调用此接口的方法
 */
@RestController
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('admin')")
public class AdminController {

    @Resource
    private AdminService adminService;

    /**
     * 根据账号搜索用户,后续改为多条件聚合查询,添加模糊查询
     * @param username
     * @return
     */
    @GetMapping("/search")
    public Response<List<User>> search(String username){
        List<User> users = adminService.searchUser(username);
        return ResponseUtil.success(users);
    }

    /**
     * 用id封号
     * @param id
     * @return
     */
    @PostMapping("/ban")
    public Response<Boolean> ban(Long id){
        if (id<0) ExceptionUtil.throwAppErr(ResponseConstants.PARAMETER_ERROR);
        boolean b = adminService.banUser(id);
        return ResponseUtil.success(b);
    }



}
