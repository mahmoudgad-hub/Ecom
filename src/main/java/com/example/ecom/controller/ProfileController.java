package com.example.ecom.controller;


import com.example.ecom.dto.ProfileDto;
import com.example.ecom.dto.UserDto;
import com.example.ecom.entity.ProfileEntity;
import com.example.ecom.entity.UserEntity;
import com.example.ecom.service.ProfileService;
import com.example.ecom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    public ProfileService  profileService;

    @GetMapping("/{userId}")
    public ProfileEntity profileGetByUserId(@PathVariable Long userId){

        return profileService.profileReadById(userId);
    }

    @PostMapping("")
    public Long profileCreate(@RequestBody ProfileDto profileDto){



        return profileService.profileCreate(profileDto);
    }

    @PutMapping("/{userId}")
    public void profileUpdateById(@PathVariable Long userId,@RequestBody ProfileDto profileDto){
        profileService.profileUpdate(userId,profileDto);
    }


}

