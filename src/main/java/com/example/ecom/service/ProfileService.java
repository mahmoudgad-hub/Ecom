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
        profileEntity.setIsGenNotification(profileDto.getIsGenNotification());
        profileEntity.setIsSound(profileDto.getIsSound());
        profileEntity.setIsVibrate(profileDto.getIsVibrate());
        profileEntity.setIsSpecialOffers(profileDto.getIsSpecialOffers());
        profileEntity.setIsPromoDiscount(profileDto.getIsPromoDiscount());
        profileEntity.setIsPayments(profileDto.getIsPayments());
        profileEntity.setIsCashBack(profileDto.getIsCashBack());
        profileEntity.setIsAppUpdate(profileDto.getIsAppUpdate());
        profileEntity.setIsNServiceAvailable(profileDto.getIsNServiceAvailable());
        profileEntity.setIsNTipsAvailable(profileDto.getIsNTipsAvailable());
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
            throw new UserExistException("No profile found for this user ID");
        }else{
            ProfileEntity profileEntityUpdated = profileEntityOptional.get();

            profileEntityUpdated.setFullName(profileDto.getFullName());
            profileEntityUpdated.setFullName(profileDto.getFullName());
            profileEntityUpdated.setDateOfBirth(profileDto.getDateOfBirth());
            profileEntityUpdated.setPhoneNumber(profileDto.getPhoneNumber());
            profileEntityUpdated.setGender(profileDto.getGender());
            profileEntityUpdated.setBalanceValue(profileDto.getBalanceValue());
            profileEntityUpdated.setIsGenNotification(profileDto.getIsGenNotification());
            profileEntityUpdated.setIsSound(profileDto.getIsSound());
            profileEntityUpdated.setIsVibrate(profileDto.getIsVibrate());
            profileEntityUpdated.setIsSpecialOffers(profileDto.getIsSpecialOffers());
            profileEntityUpdated.setIsPromoDiscount(profileDto.getIsPromoDiscount());
            profileEntityUpdated.setIsPayments(profileDto.getIsPayments());
            profileEntityUpdated.setIsCashBack(profileDto.getIsCashBack());
            profileEntityUpdated.setIsAppUpdate(profileDto.getIsAppUpdate());
            profileEntityUpdated.setIsNServiceAvailable(profileDto.getIsNServiceAvailable());
            profileEntityUpdated.setIsNTipsAvailable(profileDto.getIsNTipsAvailable());
            profileRepo.save(profileEntityUpdated);
        }


    }


}
