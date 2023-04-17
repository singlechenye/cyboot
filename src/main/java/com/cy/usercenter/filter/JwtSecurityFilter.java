package com.cy.usercenter.filter;


import com.cy.usercenter.constant.ResponseConstants;
import com.cy.usercenter.model.domain.SecurityUserDetails;
import com.cy.usercenter.model.domain.User;
import com.cy.usercenter.util.ExceptionUtil;
import com.cy.usercenter.util.JwtUtil;
import com.cy.usercenter.util.RedisCacheUtil;
import io.jsonwebtoken.Claims;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @author 86147
 * create  17/4/2023 下午3:17
 */
public class JwtSecurityFilter extends OncePerRequestFilter {


    @Resource
    private AuthenticationManager authenticationManager;

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

        User user = redisCacheUtil.getCacheObject("login:" + subject);
        if (Objects.isNull(user)) {
            ExceptionUtil.throwAppErr(ResponseConstants.NOT_LOGIN_ERROR);
        }
        SecurityUserDetails securityUserDetails = new SecurityUserDetails(user);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(securityUserDetails,null,null );
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request,response);
    }
}
