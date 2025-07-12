package com.example.ecom.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "ECOM_PROFILE_TAB",schema = "hr")
@Setter
@Getter
public class ProfileEntity {

    @Id
    @Column(name = "PROFILE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "PROFILE_SEQ_GENERATOR")
    @SequenceGenerator(name = "PROFILE_SEQ_GENERATOR", sequenceName = "HR.ECOM_PROFILE_TAB_SEQ", allocationSize = 1)
    private Long profileId;

    @Column(name = "USER_ID")
    private Long userId;

    private String fullName;

    private Date dateOfBirth;

    private Long phoneNumber;

    private Long gender;

    private Long balanceValue;

    @Column(name = "GEN_NOTIFICATION")
    private boolean isGenNotification;
    @Column(name = "SOUND")
    private boolean isSound;
    @Column(name = "VIBRATE")
    private boolean isVibrate;
    @Column(name = "SPECIAL_OFFERS")
    private boolean isSpecialOffers;
    @Column(name = "PROMO_DISCOUNT")
    private boolean isPromoDiscount;
    @Column(name = "PAYMENTS")
    private boolean isPayments;
    @Column(name = "CASHBACK")
    private boolean isCashBack;
    @Column(name = "APP_UPDATE")
    private boolean isAppUpdate;
    @Column(name = "N_SERVICE_AVAILABLE")
    private boolean isNServiceAvailable;
    @Column(name = "N_TIPS_AVAILABLE")
    private boolean isNTipsAvailable;


//    @OneToOne
//    @JoinColumn(name = "PROFILE_USER_ID", referencedColumnName = "USER_ID", unique = true)
//    @JsonBackReference
//    private UserEntity userEntity;

}


