package com.cy.cyboot;

import com.cy.cyboot.service.UserService;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class UsercenterApplicationTests {

    @Resource
    private UserService userService;


}
