package com.example.ecom.setting.AuditLog;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "ECOM_AUDIT_LOG_TAB")
@Setter
@Getter
public class AuditLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "AUDIT_LOG_SEQ_GENERATOR")
    @SequenceGenerator(name = "AUDIT_LOG_SEQ_GENERATOR", sequenceName = "A234491B.ECOM_AUDIT_LOG_TAB_SEQ", allocationSize = 1)
    @Column(name = "AUDIT_ID")
    private Long id;

    @Column(name = "TABLE_NAME")
    private String tableName;

    @Column(name = "OPERATION")
    private String operation; // CREATE, UPDATE, DELETE

    @Column(name = "ENTITY_ID")
    private String entityId;

    @Column(name = "OLD_VALUE", columnDefinition = "CLOB")
    private String oldValue;

    @Column(name = "NEW_VALUE", columnDefinition = "CLOB")
    private String newValue;

    @Column(name = "CREATED_BY")
    private Long createdBy;

    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;

    // getters/setters
}
