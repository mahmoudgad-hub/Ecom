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
/*
* public ResponseEntity<?> createCategory(@RequestBody Category categoryRequest) {
    Category savedCategory = categoryRepository.save(categoryRequest);
    CategoryResponse response = categoryService.toDto(savedCategory);

    Map<String, Object> result = new HashMap<>();
    result.put("data", response);

    return new ResponseEntity<>(result, HttpStatus.CREATED);
}
*
*  @PostMapping("")
    public Long createUser(@RequestBody UserDto userDto){
        return userService.create(userDto);
    }
* */
    @PostMapping("")
    public ResponseEntity<?> createUser(@RequestBody UserDto userDto){
          userService.create(userDto);
        Map<String, Object> result = new HashMap<>();
        result.put("data", userDto);

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable Long userId){

        userService.delete(userId);
    }

    @PutMapping("/{userId}")
    public void updateUserById(@PathVariable Long userId,@RequestBody UserDto userDto){
        userService.update(userId,userDto);
    }

    @PostMapping("/test")
    public ResponseEntity<String> test(@RequestBody UserDto  userDto) {
        validateRequest(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Product created successfully");
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
//        if (!isValid(dto.getTaxId())) {
//            errors.put("taxes", List.of("The Selected Tax Is Invalid"));
//        }
//        if (!isValid(dto.getModifierId())) {
//            errors.put("modifiers", List.of("The Selected Modifier Is Invalid"));
//        }
//        if (!isValid(dto.getDiscountId())) {
//            errors.put("discounts", List.of("The Selected Discount Is Invalid"));
//        }

        if (!errors.isEmpty()) {
            throw new ValidationException("Validation failed", errors);
        }
    }



}

