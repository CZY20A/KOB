package com.kob.backend.service.impl.utils;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import com.kob.backend.pojo.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    //账号未过期
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //未锁定
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //授权未过期
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //启用
    @Override
    public boolean isEnabled() {
        return true;
    }
}
