package com.example.ecom.controller;

import com.example.ecom.dto.UserDto;
import com.example.ecom.entity.UserEntity;
import com.example.ecom.exception.ValidationException;
import com.example.ecom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    public UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Long userId){
        UserDto userDto =   userService.readById(userId);

        Map<String, Object> result = new HashMap<>();
        result.put("data", userDto);

        return new ResponseEntity<>(result, HttpStatus.OK);

    }

//    @GetMapping("")
//    public Page<UserEntity> getAll(@RequestParam(defaultValue = "1") int pageNumber, @RequestParam(defaultValue = "5") int pageSize){
//        return userService.readAll(pageNumber,pageSize);
//    }
    @GetMapping("")
    public List<UserEntity> getAll(){

        return userService.getuserall();
    }

//    @PostMapping("")
//    public ResponseEntity<?> createUser(@RequestBody UserDto userDto){
//          userService.create(userDto);
//        Map<String, Object> result = new HashMap<>();
//        result.put("data", userDto);
//
//        return new ResponseEntity<>(result, HttpStatus.CREATED);
//    }
    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable Long userId){

        userService.delete(userId);
    }

    @PutMapping("/{userId}")
    public void updateUserById(@PathVariable Long userId,@RequestBody UserDto userDto){
        userService.update(userId,userDto);
    }

    @PostMapping("")
   public ResponseEntity<?> createUser(@RequestBody UserDto userDto)
    {

        userService.create(userDto);
        Map<String, Object> result = new HashMap<>();
        result.put("data", userDto);

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

}

