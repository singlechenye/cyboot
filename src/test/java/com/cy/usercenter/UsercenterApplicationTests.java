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

    @Test
    void contextLoads() {
        User user = new User();
        user.setUserAccount("");
        user.setUsername("");
        user.setPassword("");
        user.setImageUrl("");
        user.setGender(0);
        user.setPhone("");
        user.setEmail("");
        boolean save = userService.save(user);
        System.out.println(save);
        System.out.println(user.getId());
    }

    @Test
    void testRegister(){
        long adm = userService.register("admin", "12345678", "12345678");
        Assertions.assertEquals(-1,adm);
    }
}
