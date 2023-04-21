package com.cy.usercenter.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cy.usercenter.mapper.UserMapper;
import com.cy.usercenter.model.domain.User;
import com.cy.usercenter.service.AdminService;
import com.cy.usercenter.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;


/**
 * @author 86147
 * create  14/4/2023 上午9:38
 */
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
