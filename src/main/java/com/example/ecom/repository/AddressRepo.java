package com.example.ecom.repository;

import com.example.ecom.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface AddressRepo extends JpaRepository<AddressEntity, Long> {

    List<AddressEntity> findByUserId(Long userId);

    List<AddressEntity> findByUserIdAndIsDefaultFlag(Long userId, Boolean isDefaultFlag);
}
