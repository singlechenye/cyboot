package com.cy.cyboot.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cy.cyboot.model.security.CustomUserDetails;
import com.cy.cyboot.model.domain.User;
import com.cy.cyboot.service.UserService;
import com.cy.cyboot.mapper.UserMapper;
import com.cy.cyboot.util.JwtUtil;
import com.cy.cyboot.util.RedisCacheUtil;
import com.cy.cyboot.util.UserUtil;
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

import static com.cy.cyboot.constant.ResponseConstants.*;
import static com.cy.cyboot.constant.UserConstants.*;
import static com.cy.cyboot.util.ExceptionUtil.throwAppErr;

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
    public User Info() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Object cacheObject = redisCacheUtil.getCacheObject(REDIS_LOGIN_KEY + username);
        User user = JSON.parseObject(cacheObject.toString(), User.class);
        return user;
    }

    @Override
    public void register(String username, String password, String checkPassword){
        if (!password.equals(checkPassword)){
            throwAppErr(PARAMETER_ERROR);
        }
        checkParam(username,password);
        String encryptionPassword = encryptionPassword(password);
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("username",username);
        if (this.count(userQueryWrapper)>0) throwAppErr(REGISTER_EXIST_ERROR);
        User user = new User();
        user.setUsername(username);
        user.setPassword(encryptionPassword);
        this.save(user);
    }

    @Override
    public String login(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,password);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        if (Objects.isNull(authentication)) throwAppErr(LOGIN_PASSWORD_WRONG_ERROR);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        redisCacheUtil.setCacheObject(REDIS_LOGIN_KEY+userDetails.getUsername(),user,24*60*60, TimeUnit.SECONDS);
        return JwtUtil.createJwt(user.getUsername());
    }
    @Override
    public void Logout() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        redisCacheUtil.deleteObject(REDIS_LOGIN_KEY + userDetails.getUser().getId());
    }

    @Override
    public void dump() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long id = userDetails.getUser().getId();
        redisCacheUtil.deleteObject(REDIS_LOGIN_KEY + userDetails.getUser().getId());
        this.removeById(id);
    }

    @Override
    public void checkParam(String username,String password) {
        if (StringUtils.isAnyBlank(username, password)) {
            throwAppErr(PARAMETER_ERROR);
        }
        if (username.length()<4) throwAppErr(PARAMETER_ERROR);
        Matcher matcher = Pattern.compile(USER_ACCOUNT_MATCH).matcher(username);
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




