package com.example.ecom.service;

import com.example.ecom.entity.ConfigurationEntity;
import com.example.ecom.repository.ConfigurationRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationService {

    @Autowired
    private ConfigurationRepo configRepo;

    @Cacheable("configurations")
    public String getConfigValue(String key) {
        System.out.println(">>> Fetching config from DB for key: " + key);
        return configRepo.findById(key)
                .filter(c -> "Y".equalsIgnoreCase(c.getIsEnabledFlag()))
                .map(ConfigurationEntity::getValue)
                .orElse("NOT_FOUND");
    }
}

