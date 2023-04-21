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

    long register(String userAccount, String password, String checkPassword);

    String login(String userAccount, String password);

    void Logout();

    boolean dump();

    void checkParam(String userAccount,String password);

    String encryptionPassword(String password);

    List<User> searchUser(String username);

}
