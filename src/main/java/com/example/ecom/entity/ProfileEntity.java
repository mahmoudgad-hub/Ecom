package com.example.ecom.entity;


import com.example.ecom.setting.AuditEntity;
import com.example.ecom.setting.AuditLog.UserAuditListener;
import com.example.ecom.setting.BooleanToYNConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "ECOM_PROFILE_TAB",schema = "hr")
@EntityListeners(UserAuditListener.class)
@Setter
@Getter
public class ProfileEntity extends AuditEntity {

    @Id
    @Column(name = "PROFILE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "PROFILE_SEQ_GENERATOR")
    @SequenceGenerator(name = "PROFILE_SEQ_GENERATOR", sequenceName = "hr.ECOM_PROFILE_TAB_SEQ", allocationSize = 1)
    private Long profileId;

    @Column(name = "USER_ID")
    private Long userId;

    private String fullName;

    private Date dateOfBirth;

    private String phoneNumber;

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


//    @OneToOne
//    @JoinColumn(name = "PROFILE_USER_ID", referencedColumnName = "USER_ID", unique = true)
//    @JsonBackReference
//    private UserEntity userEntity;


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
}

