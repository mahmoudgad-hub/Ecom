package com.example.ecom.repository;

import com.example.ecom.entity.RevokedTokenEntity;
import com.example.ecom.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RevokedTokenRepo extends JpaRepository<RevokedTokenEntity, String> {

//     boolean existsByToken(String token);
    Optional<RevokedTokenEntity> findByToken(String email);
}

