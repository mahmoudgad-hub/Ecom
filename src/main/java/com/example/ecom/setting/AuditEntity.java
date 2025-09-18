package com.example.ecom.setting;

import com.example.ecom.auth.CurrentUser;
import com.example.ecom.auth.CustomUserDetails;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

@MappedSuperclass
@Setter
@Getter
public abstract class AuditEntity {


    @Column(name = "CREATED_BY", updatable = false)
    private Long createdBy;

    @Column(name = "CREATED_DATE", updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "UPDATED_BY")
    private Long updatedBy;

    @Column(name = "UPDATED_DATE")
    private LocalDateTime updatedDate;

    @PrePersist
    public void prePersist() {
        this.createdBy = CurrentUser.getUserId();
        this.createdDate = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        System.out.println("preeeeeeeeeeeeeeeee");

        this.updatedBy = CurrentUser.getUserId();
        this.updatedDate = LocalDateTime.now();
    }
}
