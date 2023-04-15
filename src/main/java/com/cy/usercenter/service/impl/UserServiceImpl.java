package com.cy.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cy.usercenter.model.domain.User;
import com.cy.usercenter.service.UserService;
import com.cy.usercenter.mapper.UserMapper;
import com.cy.usercenter.util.UserUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.cy.usercenter.constant.ResponseConstants.*;
import static com.cy.usercenter.constant.UserConstants.*;
import static com.cy.usercenter.util.ExceptionUtil.throwAppErr;
import static com.cy.usercenter.util.UserUtil.getSafetyUser;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{



    @Override
    public List<User> searchUser(String username) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(username)) userQueryWrapper.like("username",username);
        return this.list(userQueryWrapper).stream().peek(UserUtil::getSafetyUser).collect(Collectors.toList());
    }

    @Override
    public boolean banUser(long id) {
        User user = new User();
        user.setUserStatus(0);
        user.setId(id);
        return updateById(user);
    }

    @Override
    public long register(String userAccount, String password, String checkPassword){
        if (!password.equals(checkPassword)){
            throwAppErr(PARAMETER_ERROR);
        }
        checkParam(userAccount,password);
        String encryptionPassword = encryptionPassword(password);
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("userAccount",userAccount);
        if (this.count(userQueryWrapper)>0) throwAppErr(REGISTER_EXIST_ERROR);
        User user = new User();
        user.setUserAccount(userAccount);
        user.setPassword(encryptionPassword);
        this.save(user);
        return 0;
    }



    //    @Override
//    public User login(String userAccount, String password, HttpServletRequest request) {
//        checkParam(userAccount,password);
//        String encryptionPassword = encryptionPassword(password);
//        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
//        userQueryWrapper.eq("userAccount",userAccount);
//        userQueryWrapper.eq("password",encryptionPassword);
//        User user;
//        if ((user=this.getOne(userQueryWrapper))==null) throwAppErr(LOGIN_NOT_EXIST_ERROR);
//        if (!user.getPassword().equals(password)) throwAppErr(LOGIN_PASSWORD_WRONG_ERROR);
//        getSafetyUser(user);
//        request.getSession().setAttribute(USER_LOGIN_STATE,user);
//        return user;
//    }
    @Override
    public int Logout(HttpServletRequest request) {
        request.removeAttribute(USER_LOGIN_STATE);
        return 1;
    }

    @Override
    public boolean dump(int id) {
        return this.removeById(id);
    }

    @Override
    public void checkParam(String userAccount,String password) {
        if (StringUtils.isAnyBlank(userAccount, password)) {
            throwAppErr(PARAMETER_ERROR);
        }
        if (userAccount.length()<4) throwAppErr(PARAMETER_ERROR);
        Matcher matcher = Pattern.compile(USER_ACCOUNT_MATCH).matcher(userAccount);
        if (matcher.find()) {
            throwAppErr(PARAMETER_ERROR);
        }
        if (password.length() < 8) {
            throwAppErr(PARAMETER_ERROR);
        }

    }

    @Override
    public String encryptionPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }


}




