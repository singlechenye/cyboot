package com.cy.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cy.usercenter.model.domain.User;
import java.util.List;
public interface AdminService extends IService<User> {
    List<User> searchUser(String username);

    boolean banUser(long id);


}
