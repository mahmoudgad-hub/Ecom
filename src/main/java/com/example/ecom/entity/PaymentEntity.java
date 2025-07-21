package com.example.ecom.entity;

import com.example.ecom.setting.AuditEntity;
import com.example.ecom.setting.AuditLog.UserAuditListener;
import com.example.ecom.setting.BooleanToYNConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "ECOM_PAYMENT_TAB" )
@EntityListeners(UserAuditListener.class)
@Setter
@Getter
public class PaymentEntity extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "PAYMENT_SEQ_GENERATOR")
    @SequenceGenerator(name = "PAYMENT_SEQ_GENERATOR", sequenceName = "A234491B.ECOM_PAYMENT_TAB_SEQ", allocationSize = 1)
    @Column(name = "PAYMENT_ID")
    private Long paymentId;

    private Long userId;

    @Column(name = "CARD_NAME")
    private String cardName;

    @Column(name = "CARD_NUMBER")
    private String cardNumber;

    @Column(name = "EXPIRY_DATE")
    private Date expiryDate;

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean isActiveFlag;
}

