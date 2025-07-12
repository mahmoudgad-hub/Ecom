package com.example.ecom.controller;

import com.example.ecom.dto.UserDto;
import com.example.ecom.entity.UserEntity;
import com.example.ecom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    public UserService userService;

    @GetMapping("/{userId}")
    public UserEntity getUserById(@PathVariable Long userId){
        return userService.readById(userId);
    }

//    @GetMapping("")
//    public Page<UserEntity> getAll(@RequestParam(defaultValue = "1") int pageNumber, @RequestParam(defaultValue = "5") int pageSize){
//        return userService.readAll(pageNumber,pageSize);
//    }
    @GetMapping("")
    public List<UserEntity> getAll(){

        return userService.getuserall();
    }

    @PostMapping("")
    public Long createUser(@RequestBody UserDto userDto){

        return userService.create(userDto);
    }
    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable Long userId){
        userService.delete(userId);
    }

    @PutMapping("/{userId}")
    public void updateUserById(@PathVariable Long userId,@RequestBody UserDto userDto){
        userService.update(userId,userDto);
    }


}

