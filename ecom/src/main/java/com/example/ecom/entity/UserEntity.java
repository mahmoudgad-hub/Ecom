package com.example.ecom.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.apache.naming.factory.SendMailFactory;

import java.util.Date;

@Entity
@Table(name = "ECOM_USERS_TAB",schema = "hr")
@Setter
@Getter
public class UserEntity {
    @Id
    @Column(name = "USER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "USER_SEQ_GENERATOR")
    @SequenceGenerator(name = "USER_SEQ_GENERATOR", sequenceName = "ECOM_USERS_TAB_SEQ", allocationSize = 1)
    private Long userId;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    private boolean isEnabled;

    private boolean isAccountNonLocked;

    private boolean isAccountNonExpired;

    private Date lastLoginDate;

}
