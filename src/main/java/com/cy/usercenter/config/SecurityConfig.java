package com.cy.usercenter.config;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cy.usercenter.constant.ResponseConstants;
import com.cy.usercenter.filter.JwtSecurityFilter;
import com.cy.usercenter.mapper.UserMapper;
import com.cy.usercenter.model.domain.CustomUserDetails;
import com.cy.usercenter.model.domain.User;
import com.cy.usercenter.model.response.Response;
import com.cy.usercenter.util.ExceptionUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;
import java.util.Objects;

import static com.cy.usercenter.constant.ResponseConstants.*;


@Configuration
public class SecurityConfig {

    @Resource
    private  JwtSecurityFilter jwtSecurityFilter;

    @Resource
    private UserMapper userMapper;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
                userQueryWrapper.eq("username",username);
                User user = userMapper.selectOne(userQueryWrapper);
                if (Objects.isNull(user)) ExceptionUtil.throwAppErr(ResponseConstants.LOGIN_NOT_EXIST_ERROR);
                return new CustomUserDetails(user);
            }
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
                .addFilterBefore(jwtSecurityFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.setStatus(200);
                    response.setContentType("application/json");
                    Response<Object> objectResponse = new Response<>(AUTH_LACK_ERROR.getCode(), null, AUTH_LACK_ERROR.getMsg());
                    String s = JSON.toJSONString(objectResponse);
                    response.getWriter().write(s);
                })
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(200);
                    response.setContentType("application/json");
                    Response<Object> objectResponse = new Response<>(AUTH_CHECK_ERROR.getCode(), null, AUTH_CHECK_ERROR.getMsg());
                    String s = JSON.toJSONString(objectResponse);
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(s);
                });

        return httpSecurity.build();
    }

}
