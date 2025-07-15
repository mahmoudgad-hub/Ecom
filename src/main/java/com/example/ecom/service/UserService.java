package com.example.ecom.service;

import com.example.ecom.dto.UserDto;
import com.example.ecom.entity.UserEntity;
import com.example.ecom.exception.user.UserExistException;
import com.example.ecom.exception.user.UserNotFoundException;
import com.example.ecom.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    private UserEntity userMapDtoToEntity(UserDto userDto){
        UserEntity userEntity =  new UserEntity();

        userEntity.setEmail(userDto.getEmail());
        userEntity.setPassword(userDto.getPassword());
        userEntity.setIsEnabled(userDto.getIsEnabled());
        userEntity.setIsAccountNonLocked(userDto.getIsAccountNonLocked());
        userEntity.setIsAccountNonExpired(userDto.getIsAccountNonExpired());
        userEntity.setLastLoginDate(userDto.getLastLoginDate());

//        if (userEntity.getProfileEntity() != null) {
//            userEntity.getProfileEntity().setFullName(userDto.getProfileDto().getFullName());
//            userEntity.getProfileEntity().setDateOfBirth(userDto.getProfileDto().getDateOfBirth());
//            userEntity.getProfileEntity().setPhoneNumber(userDto.getProfileDto().getPhoneNumber());
//            userEntity.getProfileEntity().setGender(userDto.getProfileDto().getGender());
//            userEntity.getProfileEntity().setBalanceValue(userDto.getProfileDto().getBalanceValue());
//            userEntity.getProfileEntity().setGenNotification(userDto.getProfileDto().isGenNotification());
//            userEntity.getProfileEntity().setSound(userDto.getProfileDto().isSound());
//            userEntity.getProfileEntity().setVibrate(userDto.getProfileDto().isVibrate());
//            userEntity.getProfileEntity().setSpecialOffers(userDto.getProfileDto().isSpecialOffers());
//            userEntity.getProfileEntity().setPromoDiscount(userDto.getProfileDto().isPromoDiscount());
//            userEntity.getProfileEntity().setPayments(userDto.getProfileDto().isPayments());
//            userEntity.getProfileEntity().setCashBack(userDto.getProfileDto().isCashBack());
//            userEntity.getProfileEntity().setAppUpdate(userDto.getProfileDto().isAppUpdate());
//            userEntity.getProfileEntity().setNServiceAvailable(userDto.getProfileDto().isNServiceAvailable());
//            userEntity.getProfileEntity().setNTipsAvailable(userDto.getProfileDto().isNTipsAvailable());
//
//
//
//        }


        return userEntity;
    }

    private UserDto userMapEntityToDto(UserEntity userEntity) {
        UserDto  userDto = new UserDto();

        userDto.setEmail(userEntity.getEmail());
        userDto.setPassword(userEntity.getPassword());
        userDto.setIsEnabled(userEntity.getIsEnabled());
        userDto.setIsAccountNonLocked(userEntity.getIsAccountNonLocked());
        userDto.setIsAccountNonExpired(userEntity.getIsAccountNonExpired());
        userDto.setLastLoginDate(userEntity.getLastLoginDate());
        return userDto;
    }

    public Long  create(UserDto userDto){

        UserEntity userEntity = userMapDtoToEntity(userDto);
        userRepo.save(userEntity);
        return userEntity.getUserId();

    }

    public UserDto  readById(Long userId) {
        Optional<UserEntity> userEntityOptional= userRepo.findById(userId);

        if(userEntityOptional.isEmpty()){
            //TODO: add exception handler
           throw new UserNotFoundException("NO DATA FOUND USER");

        }else{

            return userMapEntityToDto(userEntityOptional.get());
        }
    }

    public Page<UserEntity> readAll(int pageNumber, int pageSize) {
        Page<UserEntity> userEntities = userRepo.findAll(PageRequest.of(pageNumber, pageSize));

        //       Page<UserEntity> userEntities = userRepo.findAllUsersWithRowNum(pageNumber,pageSize,PageRequest.of(pageNumber, pageSize));
        return userEntities;
    }


    public void  update(Long userId, UserDto userDto){

//        userEntity.setUserId(userId);
//        userRepo.save(userEntity);
        Optional<UserEntity> userEntityOptional= userRepo.findById(userId);
        if(userEntityOptional.isEmpty()){
            //TODO: add exception handler
            throw new UserExistException("Error");
        }else{
            UserEntity userEntityUpdated = userEntityOptional.get();
            userEntityUpdated.setEmail(userDto.getEmail());
            userEntityUpdated.setPassword(userDto.getPassword());
            userEntityUpdated.setIsEnabled(userDto.getIsEnabled());
            userEntityUpdated.setIsAccountNonLocked(userDto.getIsAccountNonLocked());
            userEntityUpdated.setIsAccountNonExpired(userDto.getIsAccountNonExpired());
            userEntityUpdated.setLastLoginDate(userDto.getLastLoginDate());
            userRepo.save(userEntityUpdated);
        }


    }

    public void  delete(Long userId){

//        userEntity.setUserId(userId);
//        userRepo.save(userEntity);
        Optional<UserEntity> userEntityOptional= userRepo.findById(userId);
        if(userEntityOptional.isEmpty()){
            //TODO: add exception handler
            throw new RuntimeException();
        }else{
            userRepo.deleteById(userId);
        }

    }
    @Transactional(readOnly = true)
    public List<UserEntity>   getuserall() {
        List<UserEntity> userEntity = userRepo.findAll();
        if(userEntity.isEmpty()){
            //TODO: add exception handler
            throw new UserNotFoundException("Test");

        }else{
            return  userEntity;
        }
    }





}
