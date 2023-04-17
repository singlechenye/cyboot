package com.cy.usercenter.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cy.usercenter.constant.ResponseConstants;
import com.cy.usercenter.filter.JwtSecurityFilter;
import com.cy.usercenter.model.domain.SecurityUserDetails;
import com.cy.usercenter.model.domain.User;
import com.cy.usercenter.service.UserService;
import com.cy.usercenter.util.ExceptionUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;


@Configuration
public class SecurityConfig {
    @Resource
    private UserService userService;
    @Resource
    private  JwtSecurityFilter jwtSecurityFilter;

    @Bean
    public PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return userAccount -> {
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq("userAccount",userAccount);
            User user = userService.getOne(userQueryWrapper);
            if (user==null) ExceptionUtil.throwAppErr(ResponseConstants.LOGIN_NOT_EXIST_ERROR);
            return new SecurityUserDetails(user);
        };
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf()
                .disable()
                .sessionManagement()
                .disable()
                .authorizeRequests()
                .antMatchers("/user/login","/user/register")
                .anonymous()
                .anyRequest()
                .authenticated()
                .and()
                .addFilterBefore(jwtSecurityFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

}
