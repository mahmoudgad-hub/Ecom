package com.example.ecom.setting.AuditLog;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepo extends JpaRepository<AuditLogEntity, Long> {
}

