package com.cy.usercenter;

import com.cy.usercenter.model.domain.User;
import com.cy.usercenter.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class UsercenterApplicationTests {

    @Resource
    private UserService userService;


}
