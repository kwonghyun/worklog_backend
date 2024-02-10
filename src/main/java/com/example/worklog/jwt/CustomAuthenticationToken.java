package com.example.worklog.jwt;

import com.example.worklog.entity.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CustomAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private final User user;

    public CustomAuthenticationToken(User user, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(user.getUsername(), credentials, authorities);
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}