package com.example.ecom.dto;


import lombok.Getter;

import java.util.Date;

@Getter
public class UserDto  {

    private String email;
    private String password;
    private boolean isEnabled;
    private boolean isAccountNonLocked;
    private boolean isAccountNonExpired;
    private Date lastLoginDate;

}
