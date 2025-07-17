package com.example.ecom.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserDto  {

    private String email;
    private String password;
    private String phoneNumber;
    private Boolean isEnabled;
    private Boolean isAccountNonLocked;
    private Boolean isAccountNonExpired;
    private Date lastLoginDate;

   // private ProfileDto  ProfileDto;
}
