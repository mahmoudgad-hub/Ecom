package com.example.ecom.dto;



import lombok.Getter;

import java.util.Date;

@Getter
public class ProfileDto {

    private Long userId;
    private String fullName;
    private Date dateOfBirth;
    private Long phoneNumber;
    private Long gender;
    private Long balanceValue;
    private boolean isGenNotification;
    private boolean isSound;
    private boolean isVibrate;
    private boolean isSpecialOffers;
    private boolean isPromoDiscount;
    private boolean isPayments;
    private boolean isCashBack;
    private boolean isAppUpdate;
    private boolean isNServiceAvailable;
    private boolean isNTipsAvailable;
}
