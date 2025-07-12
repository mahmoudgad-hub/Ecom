package com.example.ecom.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Entity
@Table(name = "ECOM_USERS_TAB",schema = "hr")
@Setter
@Getter
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "USER_SEQ_GENERATOR")
    @SequenceGenerator(name = "USER_SEQ_GENERATOR", sequenceName = "hr.ECOM_USERS_TAB_SEQ", allocationSize = 1)
    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    private boolean isEnabled;

    private boolean isAccountNonLocked;

    private boolean isAccountNonExpired;

    private Date lastLoginDate;

//    @OneToOne(mappedBy = "userEntity", cascade = CascadeType.ALL , orphanRemoval = true)
//     @JsonManagedReference
//     private ProfileEntity profileEntity;



    // private List<ProfileEntity> profileEntity = new ArrayList<>();
    //private Set<ProfileEntity> profileEntity = new HashSet<>();


}
