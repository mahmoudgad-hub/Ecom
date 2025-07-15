package com.example.ecom.setting.AuditLog;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuditLogService {
    @Autowired
    private   AuditLogRepo auditLogRepo;

    private static AuditLogRepo staticRepo;

    @PostConstruct
    public void init() {
        staticRepo = this.auditLogRepo;
    }

    public static void saveStatic(AuditLogEntity log) {
        staticRepo.save(log);
    }
}

