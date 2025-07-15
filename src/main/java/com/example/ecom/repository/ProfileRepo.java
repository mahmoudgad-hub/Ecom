package com.example.ecom.repository;


import com.example.ecom.entity.ProfileEntity;
import com.example.ecom.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepo  extends JpaRepository<ProfileEntity, Long> {
    Optional<ProfileEntity> findByUserId(Long userId);

    Optional<ProfileEntity> findByPhoneNumber(String phoneNumber);
}

