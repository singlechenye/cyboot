package com.cy.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cy.usercenter.constant.ResponseConstants;
import com.cy.usercenter.constant.UserConstants;
import com.cy.usercenter.mapper.AdminMapper;
import com.cy.usercenter.model.domain.Admin;
import com.cy.usercenter.model.domain.User;
import com.cy.usercenter.service.AdminService;
import com.cy.usercenter.service.UserService;
import com.cy.usercenter.util.ExceptionUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.cy.usercenter.constant.ResponseConstants.LOGIN_NOT_EXIST_ERROR;
import static com.cy.usercenter.util.ExceptionUtil.throwAppErr;

/**
 * @author 86147
 * create  14/4/2023 上午9:38
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper,Admin> implements AdminService {
    @Resource
    private UserService userService;
    @Override
    public List<User> searchUser(String username) {
       return userService.searchUser(username);
    }

    @Override
    public boolean banUser(long id) {
       return banUser(id);
    }

    @Override
    public boolean isAdmin(HttpServletRequest request) {
        User user;
        if ((user = (User) request.getSession().getAttribute(UserConstants.USER_LOGIN_STATE))==null) ExceptionUtil.throwAppErr(ResponseConstants.NOT_LOGIN_ERROR);
        return user.getUserRole() == UserConstants.USER_ROLE_VIP;
    }

    @Override
    public Admin login(String account, String password, HttpServletRequest request) {
        QueryWrapper<Admin> adminQueryWrapper = new QueryWrapper<>();
        adminQueryWrapper.eq("account",account);
        adminQueryWrapper.eq("password",password);
        Admin admin;
        if ((admin=this.getOne(adminQueryWrapper))==null) throwAppErr(LOGIN_NOT_EXIST_ERROR);
        return admin;
    }
}
