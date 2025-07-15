package com.example.ecom.dto;



import lombok.Getter;

import java.util.Date;

@Getter
public class ProfileDto {

    private Long userId;
    private String fullName;
    private Date dateOfBirth;
    private String phoneNumber;
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
}
