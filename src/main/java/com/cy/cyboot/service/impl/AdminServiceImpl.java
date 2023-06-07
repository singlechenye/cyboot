package com.cy.cyboot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cy.cyboot.mapper.UserMapper;
import com.cy.cyboot.model.domain.User;
import com.cy.cyboot.service.AdminService;
import com.cy.cyboot.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;

@Service
public class AdminServiceImpl extends ServiceImpl<UserMapper,User> implements AdminService {
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
}
