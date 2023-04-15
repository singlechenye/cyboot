package com.cy.usercenter.util;

import com.cy.usercenter.model.domain.User;

/**
 * @author 86147
 * create  14/4/2023 上午9:52
 */
public class UserUtil {
    public static User getSafetyUser(User user){
        user.setPassword(null);
        //user.setIsDelete(null);
        user.setUpdateTime(null);
        return user;
    }
}
