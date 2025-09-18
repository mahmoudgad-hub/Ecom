package com.example.ecom.entity;


import com.example.ecom.setting.AuditEntity;
import com.example.ecom.setting.AuditLog.UserAuditListener;
import com.example.ecom.setting.BooleanToYNConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "ECOM_USERS_TAB" )
@EntityListeners(UserAuditListener.class)
@Setter
@Getter
@JsonPropertyOrder({
        "email",
        "password",
        "phoneNumber",
        "isEnabled",
        "isAccountNonLocked",
        "isAccountNonExpired",
        "lastLoginDate",
        "fullName",
        "dateOfBirth",
        "gender",
        "balanceValue",
        "isGenNotification",
        "isSound",
        "isVibrate",
        "isSpecialOffers",
        "isPromoDiscount",
        "isPayments",
        "isCashBack",
        "isAppUpdate",
        "isNServiceAvailable",
        "isNTipsAvailable"
})
public class UserEntity extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "USER_SEQ_GENERATOR")
    @SequenceGenerator(name = "USER_SEQ_GENERATOR", sequenceName = "ECOM_USERS_TAB_SEQ", allocationSize = 1)
    @Column(name = "USER_ID")
    private Long userId;


    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean isEnabled;

     @Convert(converter = BooleanToYNConverter.class)
    private Boolean isAccountNonLocked ;//= true;

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean isAccountNonExpired;

    @Column(name = "failed_attempt" , nullable = false)
    private Integer  failedAttempt  = 0;

    private LocalDateTime lastLoginDate;

    private String otpCode;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;

    private String fullName;

    private Date dateOfBirth;


    private Long gender;

    private Long balanceValue;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "GEN_NOTIFICATION")
    private Boolean isGenNotification;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "SOUND")
    private Boolean isSound;
    @Column(name = "VIBRATE")
    private Boolean isVibrate;
    @Column(name = "SPECIAL_OFFERS")
    private Boolean isSpecialOffers;
    @Column(name = "PROMO_DISCOUNT")
    private Boolean isPromoDiscount;
    @Column(name = "PAYMENTS")
    private Boolean isPayments;
    @Column(name = "CASHBACK")
    private Boolean isCashBack;
    @Column(name = "APP_UPDATE")
    private Boolean isAppUpdate;
    @Column(name = "N_SERVICE_AVAILABLE")
    private Boolean isNServiceAvailable;
    @Column(name = "N_TIPS_AVAILABLE")
    private Boolean isNTipsAvailable;

    private String role;

    @Override
    public String toString() {
        return "ProfileEntity{" +
                "userId=" + userId +
                ", fullName='" + fullName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", phoneNumber=" + phoneNumber +
                ", gender=" + gender +
                ", balanceValue=" + balanceValue +
                '}';
    }

//    @Transient // عشان نستخدمه داخليًا بس
//    @Autowired
//    private PasswordEncoder passwordEncoder;

    @PrePersist
    @PreUpdate
    public void encodePassword() {
        if (password != null && !password.startsWith("$2a$")) { // لو مش مشفّر أصلاً
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
           password =   encoder.encode(password);
            role = "ADMIN";
        }
    }
}
