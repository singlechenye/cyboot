package com.cy.usercenter.model.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * @author 86147
 * create  17/4/2023 上午10:56
 */
public class CustomUserDetails implements UserDetails {
    private User user;

    private List<SimpleGrantedAuthority> authorities;

    public CustomUserDetails(User user) {
        this.user = user;
        Integer userRole = user.getUserRole();
        if (userRole==0) authorities.add(new SimpleGrantedAuthority("normal"));
        if (userRole==1) authorities.add(new SimpleGrantedAuthority("admin"));
    }

    public User getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserAccount();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
