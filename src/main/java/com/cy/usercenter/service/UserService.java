package com.cy.usercenter.service;

import com.cy.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


/**
* @author 86147
* @description 针对表【user】的数据库操作Service
* @createDate 2023-03-21 13:47:36
*/
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
