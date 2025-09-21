package com.example.ecom.setting.Request_logs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestLogRepo extends JpaRepository<RequestLogEntity, Long> {
}
