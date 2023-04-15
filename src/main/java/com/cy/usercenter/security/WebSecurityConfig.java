package com.cy.usercenter.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cy.usercenter.model.domain.User;
import com.cy.usercenter.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebSecurityConfig implements WebMvcConfigurer {
    @Resource
    private UserService userService;


    @Bean
    public PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return userAccount -> {
            List<GrantedAuthority> authorityList = new ArrayList<>();
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq("userAccount",userAccount);
            User user = userService.getOne(userQueryWrapper);
            if (user.getUserRole()==0){
                authorityList.add(new SimpleGrantedAuthority("ROLE_" + "normal-user"));
            }
            if (user.getUserRole()==1){
                authorityList.add(new SimpleGrantedAuthority("ROLE_" + "vip-user"));
            }
            return  new org.springframework.security.core.userdetails.User(
                    userAccount,
                    user.getPassword(),
                    authorityList
            );
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests()
                // 允许get请求/test/any，而无需认证，不配置HttpMethod默认允许所有请求类型
                .antMatchers(HttpMethod.GET, "/test/any", "/js/**", "/css/**", "/images/**", "/icon/**", "/file/**").permitAll()
                //前端提交一个remember-me参数才可以生效
                .and()
                //.httpBasic() Basic认证，和表单认证只能选一个
                // 使用表单认证页面
                .formLogin()
                .usernameParameter("userAccount")
                .passwordParameter("password")
                // 配置登录入口，默认为security自带的页面/login
                .loginProcessingUrl("/user/login")
                .successHandler(new LoginSuccessHandler())
                .failureHandler(new LoginFailureHandler())
                .and()
                .logout()
                .logoutUrl("/user/logout")
                .and()
                // post请求要关闭csrf验证,不然访问报错；实际开发中开启，需要前端配合传递其他参数
                .csrf().disable();
        httpSecurity.exceptionHandling()
                .authenticationEntryPoint(new AuthenticationEntryPoint() {
                    @Override
                    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
                        response.setStatus(502);
                    }
                })
                // 认证异常处理handler 使用的 lambda的内部的实现
                .accessDeniedHandler(new AccessDeniedHandler() {
                    @Override
                    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
                        response.setStatus(501);
                    }
                });


        //httpSecurity.headers().cacheControl().disable();
        //禁用缓存
        return httpSecurity.build();
    }

}
