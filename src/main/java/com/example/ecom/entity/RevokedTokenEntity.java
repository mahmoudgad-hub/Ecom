package com.example.ecom.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "ECOM_REVOKED_TOKENS_TAB")
@Getter
@Setter
public class RevokedTokenEntity {

    @Id
    private String token;

    @Column(name = "expiration_date")
    private Timestamp expirationDate;

}
