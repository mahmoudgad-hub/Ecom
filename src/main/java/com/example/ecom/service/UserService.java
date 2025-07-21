package com.example.ecom.service;


import com.example.ecom.Lang.MessageUtil;
import com.example.ecom.dto.UserDto;
import com.example.ecom.dto.UserDtoResponse;
import com.example.ecom.entity.UserEntity;
import com.example.ecom.exception.ValidationException;
import com.example.ecom.exception.user.UserExistException;
import com.example.ecom.exception.user.UserNotFoundException;
import com.example.ecom.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private MessageUtil messageUtil;

    public UserDtoResponse readById(Long userId) {
        UserEntity user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("NO DATA FOUND USER"));
        return mapToDtoResponse(user);
    }

    public Page<UserEntity> readAll(int pageNumber, int pageSize) {
        Page<UserEntity> userEntities = userRepo.findAll(PageRequest.of(pageNumber, pageSize));

        //       Page<UserEntity> userEntities = userRepo.findAllUsersWithRowNum(pageNumber,pageSize,PageRequest.of(pageNumber, pageSize));
        return userEntities;
    }
    /*
      public Page<UserDto> readAll(int pageNumber, int pageSize) {
        return userRepo.findAll(PageRequest.of(pageNumber, pageSize))
                .map(this::mapToDto);
    }
    *
    * */

//    @Transactional(readOnly = true)
//    public List<UserEntity> getUserAll() {
//        List<UserEntity> userEntity = userRepo.findAll();
//
//        if (userEntity.isEmpty()) {
//            //TODO: add exception handler
//            throw new UserNotFoundException("Test");
//        } else {
//            return userEntity;
//        }
//    }
    @Transactional(readOnly = true)
    public List<UserDtoResponse> getUserAll() {
        List<UserEntity> userEntities = userRepo.findAll();
        if (userEntities.isEmpty()) {
            throw new UserNotFoundException("No users found");
        }
        return userEntities.stream()
                .map(this::mapToDtoResponse)
                .collect(Collectors.toList());
    }


    public UserDtoResponse create(UserDto userDto) {
        validateRequest(userDto);
        UserEntity userEntity = mapToEntity(userDto);
        validateUniqueEmail(userEntity.getEmail());
        userRepo.save(userEntity);

        return mapToDtoResponse(userEntity);
    }

    public void update(Long userId, UserDto userDto) {
        UserEntity user = userRepo.findById(userId)
                .orElseThrow(() -> new UserExistException(messageUtil.get("user.user_not_found")));

        updateEntityFields(user, userDto);
        userRepo.save(user);
    }

    public void delete(Long userId) {
        UserEntity user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(messageUtil.get("user.user_not_found")));
        userRepo.delete(user);
    }

    private void validateRequest(UserDto dto) {
        Map<String, List<String>> errors = new HashMap<>();

        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            errors.put("email", List.of(messageUtil.get("user.email")));
        }

        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            errors.put("password", List.of(messageUtil.get("user.password")));
        }

        if (dto.getIsEnabled() == null) {
            errors.put("enabled", List.of(messageUtil.get("user.enabled")));
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(messageUtil.get("gen.validation_failed"), errors);
        }
    }

    private void validateUniqueEmail(String email) {
        if (userRepo.findByEmail(email).isPresent()) {
            throw new UserExistException(messageUtil.get("gen.mail_Already_Exists"));
        }
    }

    public void saveLastLogin(Long userid) {

        Optional<UserEntity> userEntity = userRepo.findById(userid);
        if (userEntity.isEmpty()) {
            //TODO: add exception handler
            throw new UserExistException("No found for this user ID");
        } else {
            UserEntity userEnt = userEntity.get();
            userEnt.setLastLoginDate(LocalDateTime.now());
            userRepo.save(userEnt);

        }
    }

    private UserEntity mapToEntity(UserDto dto) {
        UserEntity entity = new UserEntity();
        entity.setEmail(dto.getEmail());
        entity.setPassword(dto.getPassword());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setIsEnabled(dto.getIsEnabled());
        entity.setIsAccountNonLocked(dto.getIsAccountNonLocked());
        entity.setIsAccountNonExpired(dto.getIsAccountNonExpired());
        entity.setLastLoginDate(dto.getLastLoginDate());
        entity.setFullName(dto.getFullName());
        entity.setDateOfBirth(dto.getDateOfBirth());
        entity.setGender(dto.getGender());
        entity.setBalanceValue(dto.getBalanceValue());
        entity.setIsGenNotification(dto.getIsGenNotification());
        entity.setIsSound(dto.getIsSound());
        entity.setIsVibrate(dto.getIsVibrate());
        entity.setIsSpecialOffers(dto.getIsSpecialOffers());
        entity.setIsPromoDiscount(dto.getIsPromoDiscount());
        entity.setIsPayments(dto.getIsPayments());
        entity.setIsCashBack(dto.getIsCashBack());
        entity.setIsAppUpdate(dto.getIsAppUpdate());
        entity.setIsNServiceAvailable(dto.getIsNServiceAvailable());
        entity.setIsNTipsAvailable(dto.getIsNTipsAvailable());
        return entity;
    }

    public UserDto mapToDto(UserEntity entity) {
        UserDto dto = new UserDto();
        dto.setEmail(entity.getEmail());
        dto.setPassword(entity.getPassword());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setIsEnabled(entity.getIsEnabled());
        dto.setIsAccountNonLocked(entity.getIsAccountNonLocked());
        dto.setIsAccountNonExpired(entity.getIsAccountNonExpired());
        dto.setLastLoginDate(entity.getLastLoginDate());
        dto.setFullName(entity.getFullName());
        dto.setDateOfBirth(entity.getDateOfBirth());
        dto.setGender(entity.getGender());
        dto.setBalanceValue(entity.getBalanceValue());
        dto.setIsGenNotification(entity.getIsGenNotification());
        dto.setIsSound(entity.getIsSound());
        dto.setIsVibrate(entity.getIsVibrate());
        dto.setIsSpecialOffers(entity.getIsSpecialOffers());
        dto.setIsPromoDiscount(entity.getIsPromoDiscount());
        dto.setIsPayments(entity.getIsPayments());
        dto.setIsCashBack(entity.getIsCashBack());
        dto.setIsAppUpdate(entity.getIsAppUpdate());
        dto.setIsNServiceAvailable(entity.getIsNServiceAvailable());
        dto.setIsNTipsAvailable(entity.getIsNTipsAvailable());
        return dto;
    }

    public UserDtoResponse mapToDtoResponse(UserEntity entity) {
        UserDtoResponse dto = new UserDtoResponse();
        dto.setEmail(entity.getEmail());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setIsEnabled(entity.getIsEnabled());
        dto.setIsAccountNonLocked(entity.getIsAccountNonLocked());
        dto.setIsAccountNonExpired(entity.getIsAccountNonExpired());
        dto.setLastLoginDate(entity.getLastLoginDate());
        dto.setFullName(entity.getFullName());
        dto.setDateOfBirth(entity.getDateOfBirth());
        dto.setGender(entity.getGender());
        dto.setBalanceValue(entity.getBalanceValue());
        dto.setIsGenNotification(entity.getIsGenNotification());
        dto.setIsSound(entity.getIsSound());
        dto.setIsVibrate(entity.getIsVibrate());
        dto.setIsSpecialOffers(entity.getIsSpecialOffers());
        dto.setIsPromoDiscount(entity.getIsPromoDiscount());
        dto.setIsPayments(entity.getIsPayments());
        dto.setIsCashBack(entity.getIsCashBack());
        dto.setIsAppUpdate(entity.getIsAppUpdate());
        dto.setIsNServiceAvailable(entity.getIsNServiceAvailable());
        dto.setIsNTipsAvailable(entity.getIsNTipsAvailable());
        return dto;
    }

    private void updateEntityFields(UserEntity entity, UserDto dto) {
        entity.setEmail(dto.getEmail());
        entity.setPassword(dto.getPassword());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setIsEnabled(dto.getIsEnabled());
        entity.setIsAccountNonLocked(dto.getIsAccountNonLocked());
        entity.setIsAccountNonExpired(dto.getIsAccountNonExpired());
        entity.setLastLoginDate(dto.getLastLoginDate());
        entity.setFullName(dto.getFullName());
        entity.setDateOfBirth(dto.getDateOfBirth());
        entity.setGender(dto.getGender());
        entity.setBalanceValue(dto.getBalanceValue());
        entity.setIsGenNotification(dto.getIsGenNotification());
        entity.setIsSound(dto.getIsSound());
        entity.setIsVibrate(dto.getIsVibrate());
        entity.setIsSpecialOffers(dto.getIsSpecialOffers());
        entity.setIsPromoDiscount(dto.getIsPromoDiscount());
        entity.setIsPayments(dto.getIsPayments());
        entity.setIsCashBack(dto.getIsCashBack());
        entity.setIsAppUpdate(dto.getIsAppUpdate());
        entity.setIsNServiceAvailable(dto.getIsNServiceAvailable());
        entity.setIsNTipsAvailable(dto.getIsNTipsAvailable());
    }


}
