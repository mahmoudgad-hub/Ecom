package com.example.ecom.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
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
public class UserDto {


    private String email;
    private String password;
    private String phoneNumber;
    private Boolean isEnabled;
    private Boolean isAccountNonLocked;
    private Boolean isAccountNonExpired;
    private LocalDateTime lastLoginDate;
    private String fullName;
    private Date dateOfBirth;
    private Long gender;
    private Long balanceValue;
    private Boolean isGenNotification;
    private Boolean isSound;
    private Boolean isVibrate;
    private Boolean isSpecialOffers;
    private Boolean isPromoDiscount;
    private Boolean isPayments;
    private Boolean isCashBack;
    private Boolean isAppUpdate;
    private Boolean isNServiceAvailable;
    private Boolean isNTipsAvailable;

    @JsonIgnore
    private Long userId;
    @JsonIgnore
    private String otpCode;
    @JsonIgnore
    private LocalDateTime createdAt;
    @JsonIgnore
    private LocalDateTime expiresAt;

}
