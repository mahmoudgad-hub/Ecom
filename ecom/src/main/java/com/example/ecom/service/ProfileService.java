package com.example.ecom.service;

import com.example.ecom.dto.ProfileDto;
import com.example.ecom.dto.UserDto;
import com.example.ecom.entity.ProfileEntity;
import com.example.ecom.entity.UserEntity;
import com.example.ecom.exception.user.UserExistException;
import com.example.ecom.exception.user.UserNotFoundException;
import com.example.ecom.repository.ProfileRepo;
import com.example.ecom.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepo profileRepo;

    @Autowired
    private UserRepo userRepo;

    private ProfileEntity profileMapDtoToEntity(ProfileDto profileDto){
        ProfileEntity profileEntity =  new ProfileEntity();

        profileEntity.setUserId(profileDto.getUserId());
        profileEntity.setFullName(profileDto.getFullName());
        profileEntity.setFullName(profileDto.getFullName());
        profileEntity.setDateOfBirth(profileDto.getDateOfBirth());
        profileEntity.setPhoneNumber(profileDto.getPhoneNumber());
        profileEntity.setGender(profileDto.getGender());
        profileEntity.setBalanceValue(profileDto.getBalanceValue());
        profileEntity.setGenNotification(profileDto.isGenNotification());
        profileEntity.setSound(profileDto.isSound());
        profileEntity.setVibrate(profileDto.isVibrate());
        profileEntity.setSpecialOffers(profileDto.isSpecialOffers());
        profileEntity.setPromoDiscount(profileDto.isPromoDiscount());
        profileEntity.setPayments(profileDto.isPayments());
        profileEntity.setCashBack(profileDto.isCashBack());
        profileEntity.setAppUpdate(profileDto.isAppUpdate());
        profileEntity.setNServiceAvailable(profileDto.isNServiceAvailable());
        profileEntity.setNTipsAvailable(profileDto.isNTipsAvailable());

        return profileEntity;
    }

    public Long  profileCreate(ProfileDto profileDto){
        ProfileEntity profileEntity = profileMapDtoToEntity(profileDto);

        if(profileEntity.getUserId() == null){
            //TODO: add exception handler
            throw new UserNotFoundException("NO DATA FOUND USER ID");

        }else{


           if ( userRepo.findById(profileEntity.getUserId()).isEmpty()){
                //TODO: add exception handler
                throw new UserExistException("User Id Not exists");
            }else {

               Optional<ProfileEntity> profileEntityOptional = profileRepo.findByUserId(profileEntity.getUserId());
               if (profileEntityOptional.isPresent()) {
                   //TODO: add exception handler
                   throw new UserExistException("Cannot create profile for this user because it already exists");
               } else {
                   profileRepo.save(profileEntity);
                   return profileEntity.getProfileId();
               }
           }
        }
    }

    public ProfileEntity  profileReadById(Long userId) {
        Optional<ProfileEntity> userEntityOptional= profileRepo.findByUserId(userId);
        if(userEntityOptional.isEmpty()){
            //TODO: add exception handler
            throw new UserNotFoundException("NO DATA FOUND Profile");

        }else{
            return userEntityOptional.get();
        }
    }

    public void  profileUpdate(Long userId, ProfileDto  profileDto){

//        userEntity.setUserId(userId);
//        userRepo.save(userEntity);
        Optional<ProfileEntity> profileEntityOptional= profileRepo.findByUserId(userId);
        if(profileEntityOptional.isEmpty()){
            //TODO: add exception handler
            throw new UserExistException("Invalid User Id");
        }else{
            ProfileEntity profileEntityUpdated = profileEntityOptional.get();

            profileEntityUpdated.setFullName(profileDto.getFullName());
            profileEntityUpdated.setFullName(profileDto.getFullName());
            profileEntityUpdated.setDateOfBirth(profileDto.getDateOfBirth());
            profileEntityUpdated.setPhoneNumber(profileDto.getPhoneNumber());
            profileEntityUpdated.setGender(profileDto.getGender());
            profileEntityUpdated.setBalanceValue(profileDto.getBalanceValue());
            profileEntityUpdated.setGenNotification(profileDto.isGenNotification());
            profileEntityUpdated.setSound(profileDto.isSound());
            profileEntityUpdated.setVibrate(profileDto.isVibrate());
            profileEntityUpdated.setSpecialOffers(profileDto.isSpecialOffers());
            profileEntityUpdated.setPromoDiscount(profileDto.isPromoDiscount());
            profileEntityUpdated.setPayments(profileDto.isPayments());
            profileEntityUpdated.setCashBack(profileDto.isCashBack());
            profileEntityUpdated.setAppUpdate(profileDto.isAppUpdate());
            profileEntityUpdated.setNServiceAvailable(profileDto.isNServiceAvailable());
            profileEntityUpdated.setNTipsAvailable(profileDto.isNTipsAvailable());
            profileRepo.save(profileEntityUpdated);
        }


    }


}
