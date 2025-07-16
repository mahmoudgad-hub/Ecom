package com.example.ecom.entity;


import com.example.ecom.setting.AuditEntity;
import com.example.ecom.setting.AuditLog.UserAuditListener;
import com.example.ecom.setting.BooleanToYNConverter;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "ECOM_USERS_TAB",schema = "A234491B")
@EntityListeners(UserAuditListener.class)
@Setter
@Getter
public class UserEntity extends AuditEntity {
    @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "USER_SEQ_GENERATOR")
    @SequenceGenerator(name = "USER_SEQ_GENERATOR", sequenceName = "A234491B.ECOM_USERS_TAB_SEQ", allocationSize = 1)
    @Column(name = "USER_ID")
    private Long userId;


    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean isEnabled;

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean isAccountNonLocked;

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean isAccountNonExpired;

    private Date lastLoginDate;

    private String otpCode;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;

//    @OneToOne(mappedBy = "userEntity", cascade = CascadeType.ALL , orphanRemoval = true)
//     @JsonManagedReference
//     private ProfileEntity profileEntity;



    // private List<ProfileEntity> profileEntity = new ArrayList<>();
    //private Set<ProfileEntity> profileEntity = new HashSet<>();


}
