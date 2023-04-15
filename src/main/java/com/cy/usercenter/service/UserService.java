package com.cy.usercenter.service;

import com.cy.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
* @author 86147
* @description 针对表【user】的数据库操作Service
* @createDate 2023-03-21 13:47:36
*/
public interface UserService extends IService<User> {

    long register(String userAccount, String password, String checkPassword);

//    User login(String userAccount, String password, HttpServletRequest request);

    int Logout(HttpServletRequest request);

    boolean dump(int id);

    void checkParam(String userAccount,String password);

    String encryptionPassword(String password);

    List<User> searchUser(String username);

    boolean banUser(long id);

}
