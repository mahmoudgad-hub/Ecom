package com.example.ecom.setting.AuditLog;

import com.example.ecom.auth.CurrentUser;
import jakarta.persistence.*;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

public class UserAuditListener {

    @PrePersist
    public void prePersist(Object entity) {
        createAudit(entity, "CREATE", null);
    }

    @PreUpdate
    public void preUpdate(Object entity) {
        createAudit(entity, "UPDATE", null); // يمكنك تحسينها لاحقًا بإضافة القيمة القديمة
    }

    @PreRemove
    public void preRemove(Object entity) {
        createAudit(entity, "DELETE", null);
    }

    private void createAudit(Object entity, String operation, String oldValue) {
        AuditLogEntity log = new AuditLogEntity();
        log.setTableName(entity.getClass().getSimpleName());
        log.setOperation(operation);
        log.setEntityId(getEntityId(entity));
        log.setOldValue(oldValue);
        log.setNewValue(entity.toString());
        log.setCreatedBy(getCurrentUser());
        log.setCreatedDate(LocalDateTime.now());

        AuditLogService.saveStatic(log);
    }

    private String getEntityId(Object entity) {
        try {
            Field idField = entity.getClass().getDeclaredField("userId");
            idField.setAccessible(true);
            return String.valueOf(idField.get(entity));
        } catch (Exception e) {
            return null;
        }
    }

    private Long getCurrentUser() {

        return CurrentUser.getUserId(); // أو خده من Spring Security JWT Token
    }
}

