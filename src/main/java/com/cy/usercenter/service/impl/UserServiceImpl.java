package com.cy.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cy.usercenter.model.domain.CustomUserDetails;
import com.cy.usercenter.model.domain.User;
import com.cy.usercenter.service.UserService;
import com.cy.usercenter.mapper.UserMapper;
import com.cy.usercenter.util.JwtUtil;
import com.cy.usercenter.util.RedisCacheUtil;
import com.cy.usercenter.util.UserUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.cy.usercenter.constant.ResponseConstants.*;
import static com.cy.usercenter.constant.UserConstants.*;
import static com.cy.usercenter.util.ExceptionUtil.throwAppErr;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private RedisCacheUtil redisCacheUtil;


    @Override
    public List<User> searchUser(String username) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(username)) userQueryWrapper.like("username",username);
        return this.list(userQueryWrapper).stream().map(UserUtil::getSafetyUser).collect(Collectors.toList());
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

    @Override
    public String login(String userAccount, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userAccount,password);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        if (Objects.isNull(authentication)) throwAppErr(LOGIN_PASSWORD_WRONG_ERROR);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        redisCacheUtil.setCacheObject(REDIS_LOGIN_KEY+user.getId(),user,24*60*60, TimeUnit.SECONDS);
        return JwtUtil.createJwt(user.getUserAccount());
    }
    @Override
    public void Logout() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        redisCacheUtil.deleteObject(REDIS_LOGIN_KEY+userDetails.getUser().getId());
    }

    @Override
    public boolean dump() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long id = userDetails.getUser().getId();
        redisCacheUtil.deleteObject(REDIS_LOGIN_KEY+id);
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




