package com.example.ecom.controller;


import com.example.ecom.Lang.MessageUtil;
import com.example.ecom.dto.UserDto;
import com.example.ecom.dto.UserDtoResponse;
import com.example.ecom.entity.AddressEntity;
import com.example.ecom.entity.UserEntity;
import com.example.ecom.repository.AddressRepo;
import com.example.ecom.repository.UserRepo;
import com.example.ecom.service.UserService;
import com.example.ecom.setting.AuditLog.AuditLogEntity;
import com.example.ecom.setting.AuditLog.AuditLogRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/data/sync")
public class SyncController {

    @Autowired
    public UserRepo  userRepo;

    @Autowired
    public AddressRepo addressRepo;

    @Autowired
    public AuditLogRepo auditLogRepo;

    @Autowired
    public UserService userService ;

    @Autowired
    private MessageUtil messageUtil;


    @GetMapping("/allData")
    public ResponseEntity<Map<String, Object>> getAllUsers() {
        List<UserEntity> userEntities = userRepo.findAll();
        List<AddressEntity> addressEntities   = addressRepo.findAll();
        List<AuditLogEntity>   auditLogEntities  = auditLogRepo.findAll();



        Map<String, Object> response = new HashMap<>();
        response.put("User", userEntities);
        response.put("Address", addressEntities);
        response.put("AuditLog", auditLogEntities);


        return ResponseEntity.ok(response);
    }

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
