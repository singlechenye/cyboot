package com.cy.cyboot.service;

import com.cy.cyboot.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface UserService extends IService<User> {

    void register(String username, String password, String checkPassword);

    String login(String username, String password);

    void Logout();

    void dump();

    void checkParam(String username,String password);

    String encryptionPassword(String password);

    List<User> searchUser(String username);

    User Info();
}
