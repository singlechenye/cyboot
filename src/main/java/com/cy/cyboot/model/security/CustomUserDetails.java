package com.cy.cyboot.model.security;

import com.cy.cyboot.model.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 这里面封装了对数据库权限字段的处理,根据数字给予对应的角色
 */
public class CustomUserDetails implements UserDetails {

    private final User user;

    private final List<GrantedAuthority> authorities;

    public CustomUserDetails(User user) {
        this.user = user;
        Integer userRole = user.getUserRole();
        authorities =new ArrayList<>();
        if (userRole==0) authorities.add(new SimpleGrantedAuthority("normal"));
        else if (userRole==1) authorities.add(new SimpleGrantedAuthority("admin"));
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
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.getUserStatus()==0;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getIsDelete()==0;
    }
}
