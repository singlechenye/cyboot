package com.cy.cyboot.util;

import com.cy.cyboot.model.domain.User;

public class UserUtil {
    public static User getSafetyUser(User user){
        user.setPassword(null);
        user.setIsDelete(null);
        user.setUpdateTime(null);
        return user;
    }
}
