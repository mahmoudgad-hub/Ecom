package com.example.ecom.controller;

import com.example.ecom.Lang.MessageUtil;
import com.example.ecom.dto.UserDto;
import com.example.ecom.dto.UserDtoResponse;
import com.example.ecom.entity.UserEntity;
import com.example.ecom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private MessageUtil messageUtil;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Long userId){
        // public ResponseEntity<Map<String, Object>> getUserById(@PathVariable Long userId) {
        UserDtoResponse userDtoResponse =   userService.readById(userId);

        Map<String, Object> result = new HashMap<>();
        result.put("data", userDtoResponse);

        return ResponseEntity.ok(result);

    }

//    @GetMapping("")
//    public Page<UserEntity> getAll(@RequestParam(defaultValue = "1") int pageNumber, @RequestParam(defaultValue = "5") int pageSize){
//        return userService.readAll(pageNumber,pageSize);
//    }
  /*
    public ResponseEntity<?>  getAll(){
        List<UserDto> userDto  = userService.getUserAll();
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }*/
@GetMapping("")
public ResponseEntity<Map<String, Object>> getAllUsers() {
    List<UserDtoResponse> users = userService.getUserAll();

    Map<String, Object> response = new HashMap<>();
    response.put("data", users);
    response.put("count", users.size());

    return ResponseEntity.ok(response);
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
    public ResponseEntity<Map<String, Object>> deleteUserById(@PathVariable Long userId) {
        userService.delete(userId);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "User deleted successfully");

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Map<String, Object>> updateUserById(@PathVariable Long userId, @RequestBody UserDto userDto) {
        userService.update(userId, userDto);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "User updated successfully");

        return ResponseEntity.ok(response);
    }

    @PostMapping("")
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody UserDto userDto) {
        UserDtoResponse userDtoResponse =  userService.create(userDto);



        Map<String, Object> response = new HashMap<>();
        response.put("data", userDtoResponse);
        response.put("message", messageUtil.get("user.user_created_success"));

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


}

