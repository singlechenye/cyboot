package com.cy.cyboot.filter;


import com.alibaba.fastjson.JSON;
import com.cy.cyboot.constant.ResponseConstants;
import com.cy.cyboot.model.security.CustomUserDetails;
import com.cy.cyboot.model.domain.User;
import com.cy.cyboot.util.ExceptionUtil;
import com.cy.cyboot.util.JwtUtil;
import com.cy.cyboot.util.RedisCacheUtil;
import io.jsonwebtoken.Claims;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

import static com.cy.cyboot.constant.UserConstants.REDIS_LOGIN_KEY;

@Component
public class JwtSecurityFilter extends OncePerRequestFilter {

    @Resource
    private RedisCacheUtil redisCacheUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("token");
        if (Strings.isBlank(token)) {
            filterChain.doFilter(request,response);
            return;
        }
        String subject = null;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            subject = claims.getSubject();
        } catch (Exception e) {
            ExceptionUtil.throwAppErr(ResponseConstants.PARAMETER_ERROR);
        }

        Object cacheObject = redisCacheUtil.getCacheObject(REDIS_LOGIN_KEY + subject);
        User user = JSON.parseObject(cacheObject.toString(), User.class);


        if (Objects.isNull(user)) {
            ExceptionUtil.throwAppErr(ResponseConstants.NOT_LOGIN_ERROR);
        }
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(customUserDetails,null,customUserDetails.getAuthorities() );
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request,response);
    }
}
