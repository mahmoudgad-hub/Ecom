package com.example.ecom.entity;

import com.example.ecom.auth.CurrentUser;
import com.example.ecom.setting.AuditEntity;
import com.example.ecom.setting.AuditLog.UserAuditListener;
import com.example.ecom.setting.BooleanToYNConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Entity
@Table(name = "ECOM_ADDRESS_TAB" )
@EntityListeners(UserAuditListener.class)
@Setter
@Getter
public class AddressEntity extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "ADDRESS_SEQ_GENERATOR")
    @SequenceGenerator(name = "ADDRESS_SEQ_GENERATOR", sequenceName = "ECOM_ADDRESS_TAB_SEQ", allocationSize = 1)
    @Column(name = "ADDRESS_ID")
    private Long addressId;

    private Long userId;

    @Column(name = "ADDRESS_NAME")
    private String addressName;
    @Column(name = "LATITUDE")
    private Long latitude  ;
    @Column(name = "LONGITUDE")
    private Long longitude;

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean isDefaultFlag;


    @PrePersist
    //@PreUpdate
    public void encodePassword() {
           userId = CurrentUser.getUserId();
        }



    @Override
    public String toString() {
        return "AddressEntity{" +
                   "addressId=" + addressId +
                ", userId='" + userId +
                ", addressName=" + addressName +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", isDefaultFlag=" + isDefaultFlag +
                '}';
    }
    }





