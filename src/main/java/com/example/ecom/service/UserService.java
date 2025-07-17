package com.example.ecom.service;

import com.example.ecom.dto.UserDto;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    private UserEntity userMapDtoToEntity(UserDto userDto){
        UserEntity userEntity =  new UserEntity();

        userEntity.setEmail(userDto.getEmail());
        userEntity.setPassword(userDto.getPassword());
        userEntity.setPhoneNumber(userDto.getPhoneNumber());
        userEntity.setIsEnabled(userDto.getIsEnabled());
        userEntity.setIsAccountNonLocked(userDto.getIsAccountNonLocked());
        userEntity.setIsAccountNonExpired(userDto.getIsAccountNonExpired());
        userEntity.setLastLoginDate(userDto.getLastLoginDate());

        return userEntity;
    }

    private UserDto userMapEntityToDto(UserEntity userEntity) {
        UserDto  userDto = new UserDto();

        userDto.setEmail(userEntity.getEmail());
        userDto.setPassword(userEntity.getPassword());
        userDto.setPhoneNumber(userEntity.getPhoneNumber());
        userDto.setIsEnabled(userEntity.getIsEnabled());
        userDto.setIsAccountNonLocked(userEntity.getIsAccountNonLocked());
        userDto.setIsAccountNonExpired(userEntity.getIsAccountNonExpired());
        userDto.setLastLoginDate(userEntity.getLastLoginDate());
        return userDto;
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


    public void create(UserDto userDto){
        validateRequest(userDto);
        UserEntity userEntity = userMapDtoToEntity(userDto);
        validateBusiness(userEntity);
        userRepo.save(userEntity);
        // return userEntity.getUserId();
    }

    private void validateRequest(UserDto dto) {
        Map<String, List<String>> errors = new HashMap<>();

        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            errors.put("email", List.of("The Email field is required."));
        }
        if (dto.getIsEnabled() == null) {
            errors.put("enabled", List.of("The Enabled field is required."));
        }
        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            errors.put("password", List.of("The Password field is required."));
        }

        if (!errors.isEmpty()) {
            throw new ValidationException("Validation failed", errors);
        }
    }
    private void validateBusiness(UserEntity userEntity) {
      //  Map<String, List<String>> errors = new HashMap<>();


        if (userRepo.findByEmail(userEntity.getEmail()).isPresent()) {
           // errors.put("Email", List.of("Email Already Exists"));
            throw new UserExistException("Email Already Exists");
        }

//        if (!errors.isEmpty()) {
//            throw new ValidationException("Validation failed", errors);
//        }
    }




}
