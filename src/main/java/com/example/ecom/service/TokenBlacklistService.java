package com.example.ecom.service;

import com.example.ecom.entity.RevokedTokenEntity;
import com.example.ecom.entity.UserEntity;
import com.example.ecom.repository.RevokedTokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class TokenBlacklistService {

    @Autowired
    private RevokedTokenRepo revokedTokenRepo;

    public void addToBlacklist(String token, Date expirationDate) {
        RevokedTokenEntity revokedTokenEntity = new RevokedTokenEntity();
        revokedTokenEntity.setToken(token);
        revokedTokenEntity.setExpirationDate(new Timestamp(expirationDate.getTime()));
        revokedTokenRepo.save(revokedTokenEntity);
    }

    public boolean isBlacklisted(String token) {
//        return revokedTokenRepo.existsByToken(token);

        Optional<RevokedTokenEntity> revokedTokenEntities =    revokedTokenRepo.findByToken(token) ;
        if (revokedTokenEntities.isEmpty()){
            return  true;
        }else{
            return false;
        }

    }
}