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

    private UserEntity mapDtoToEntity(UserDto userDto){
        UserEntity userEntity =  new UserEntity();
        userEntity.setEmail(userDto.getEmail());
        userEntity.setPassword(userDto.getPassword());
        userEntity.setEnabled(userDto.isEnabled());
        userEntity.setAccountNonLocked(userDto.isAccountNonLocked());
        userEntity.setAccountNonExpired(userDto.isAccountNonExpired());
        userEntity.setLastLoginDate(userDto.getLastLoginDate());
        return userEntity;
    }

    public Long  create(UserDto userDto){

        UserEntity userEntity = mapDtoToEntity(userDto);
        userRepo.save(userEntity);
        return userEntity.getUserId();

    }

    public UserEntity  readById(Long userId) {
        Optional<UserEntity> userEntityOptional= userRepo.findById(userId);
        if(userEntityOptional.isEmpty()){
            //TODO: add exception handler
           throw new UserNotFoundException("Test");

        }else{
            return userEntityOptional.get();
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
            userEntityUpdated.setEnabled(userDto.isEnabled());
            userEntityUpdated.setAccountNonLocked(userDto.isAccountNonLocked());
            userEntityUpdated.setAccountNonExpired(userDto.isAccountNonExpired());
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
