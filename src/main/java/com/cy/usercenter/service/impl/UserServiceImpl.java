package com.cy.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cy.usercenter.model.domain.User;
import com.cy.usercenter.service.UserService;
import com.cy.usercenter.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.cy.usercenter.constant.ResponseConstants.*;
import static com.cy.usercenter.constant.UserConstants.USER_PASS_SALT;
import static com.cy.usercenter.constant.UserConstants.USER_LOGIN_STATE;
import static com.cy.usercenter.util.ExceptionUtil.throwAppErr;

/**
* @author 86147
* @description 针对表【user】的数据库操作Service实现
* @createDate 2023-03-21 13:47:36
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{

    @Override
    public long register(String userAccount, String password, String checkPassword){
        if (userAccount.length()<4||password.length()<8|| !password.equals(checkPassword)){
            throwAppErr(CODE_PARAMETER_ERROR,MSG_PARAMETER_ERROR);
        }
        if (!userAccount.matches("/^[a-zA-Z0-9_-]{4,16}$/")){
            throwAppErr(CODE_PARAMETER_ERROR,MSG_PARAMETER_ERROR);
        }
        String md5Password = DigestUtils.md5DigestAsHex((USER_PASS_SALT + userAccount).getBytes());
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("userAccount",userAccount);
        if (this.count(userQueryWrapper)>0) throwAppErr(CODE_EXIST_ERROR,MSG_EXIST_ERROR);
        User user = new User();
        user.setUserAccount(userAccount);
        user.setPassword(password);
        this.save(user);
        return 0;
    }

    @Override
    public User login(String userAccount, String password, HttpServletRequest request) {
        if (StringUtils.isAnyBlank(userAccount, password)) {
            throwAppErr(CODE_PARAMETER_ERROR,MSG_PARAMETER_ERROR);
        }
        if (userAccount.length() < 4) {
            throwAppErr(CODE_PARAMETER_ERROR,MSG_PARAMETER_ERROR);
        }
        if (password.length() < 8) {
            throwAppErr(CODE_PARAMETER_ERROR,MSG_PARAMETER_ERROR);
        }
        // 账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throwAppErr(CODE_PARAMETER_ERROR,MSG_PARAMETER_ERROR);
        }
        String md5Password = DigestUtils.md5DigestAsHex((USER_PASS_SALT + userAccount).getBytes());
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("userAccount",userAccount);
        userQueryWrapper.eq("password",password);
        User user;
        if ((user=this.getOne(userQueryWrapper))==null) throwAppErr(CODE_NOT_EXIST_ERROR,MSG_NOT_EXIST_ERROR);
        getSafetyUser(user);
        request.getSession().setAttribute(USER_LOGIN_STATE,user);
        System.out.println(user);
        return user;
    }
    @Override
    public User getSafetyUser(User user) {
        user.setPassword(null);
        user.setIsDelete(null);
        user.setUpdateTime(null);
        return user;
    }
    @Override
    public int Logout(HttpServletRequest request) {
        request.removeAttribute(USER_LOGIN_STATE);
        return 1;
    }

}




