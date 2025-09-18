package com.example.ecom.repository;

import com.example.ecom.entity.ConfigurationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ConfigurationRepo extends JpaRepository<ConfigurationEntity, String> {
}

