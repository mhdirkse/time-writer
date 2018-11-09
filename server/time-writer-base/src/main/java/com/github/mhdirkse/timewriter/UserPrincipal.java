package com.github.mhdirkse.timewriter;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.github.mhdirkse.timewriter.model.UserInfo;

public class UserPrincipal implements UserDetails {
    private static final long serialVersionUID = 2846857548165677180L;
    private UserInfo user = null;

    public UserPrincipal() {
    }

    public UserPrincipal(UserInfo user) {
        this.user = user;
    }

    public boolean hasUser() {
        return user != null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO Auto-generated method stub
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
