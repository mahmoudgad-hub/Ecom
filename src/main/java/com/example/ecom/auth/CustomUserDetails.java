package com.example.ecom.auth;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {


    @Getter
    private Long userId;
    private String username;
    private String password;
    @Getter
    private String fullName;
    @Getter
    private String phoneNumber;

    private Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(Long userId, String username, String password, String fullname,String phoneNumber,
                             Collection<? extends GrantedAuthority> authorities) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.fullName = fullname;
        this.phoneNumber = phoneNumber;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

}

