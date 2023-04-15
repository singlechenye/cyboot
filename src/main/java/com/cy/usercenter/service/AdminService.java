package com.cy.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cy.usercenter.model.domain.Admin;
import com.cy.usercenter.model.domain.User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
public interface AdminService extends IService<Admin> {
    List<User> searchUser(String username);
    boolean banUser(long id);

    boolean isAdmin(HttpServletRequest request);

    Admin login(String account, String password, HttpServletRequest request);
}
