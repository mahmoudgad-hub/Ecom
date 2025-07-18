package com.example.ecom.controller;

import com.example.ecom.Lang.MessageUtil;
import com.example.ecom.entity.UserEntity;
import com.example.ecom.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {


    @Autowired
    private MessageUtil messageUtil;
    @Autowired
    private UserRepo userRepo;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");

        UserEntity user = userRepo.findByEmail(email).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.UNAUTHORIZED, messageUtil.get("login.mail_Not_Found")));

        if (!user.getPassword().equals(password)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, messageUtil.get("login.invalid_email_or_password"));

        }

        return Map.of(
                "message", "Login successful",
                "user", user
        );
    }
}
