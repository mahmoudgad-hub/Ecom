package com.example.ecom.controller;

import com.example.ecom.auth.CustomUserDetails;
import com.example.ecom.auth.config.JwtConfig;
import com.example.ecom.auth.config.JwtUtil;
import com.example.ecom.dto.UserDto;
import com.example.ecom.dto.UserDtoResponse;
import com.example.ecom.service.CustomUserDetailsService;
import com.example.ecom.service.TokenBlacklistService;
import com.example.ecom.service.UserService;
import com.example.ecom.whatsappotp.service.OtpService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private TokenBlacklistService blacklistService;

    @Autowired
    private UserService userService;

    @Autowired
    private  OtpService otpService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        CustomUserDetails customUserDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(request.getUsername());

        String generateToken = jwtUtil.generateToken(customUserDetails.getUsername(), customUserDetails.getUserId(), customUserDetails.getFullName());
//        String refreshToken = jwtUtil.generateRefreshToken((CustomUserDetails) customUserDetailsService.loadUserByUsername(request.getUsername()));
        String refreshToken = jwtUtil.generateRefreshToken(customUserDetails);
       // get info user
        UserDtoResponse userDtoResponse =   userService.readById(customUserDetails.getUserId());
        userService.saveLastLogin(customUserDetails.getUserId());
       //
        AuthResponse response = new AuthResponse(
                "Bearer",
                String.valueOf(jwtConfig.getAccessExpiration()),
                generateToken,
                refreshToken
        );


        Map<String, Object> result = new LinkedHashMap<>();
        result.put("token",response /*Map.of(
                "token_type", "Bearer",
                "expires_in", jwtConfig.getAccessExpiration(),
                "access_token", generateToken,
                "refresh_token", refreshToken
        )*/);
        result.put("user", userDtoResponse);

        return ResponseEntity.ok(result);
//        TokenWrapper tokenWrapper = new TokenWrapper(response);
//        return ResponseEntity.ok(tokenWrapper);


        //        response.put("message", messageUtil.get("user.user_created_success"));
//
//        return new ResponseEntity<>(response, HttpStatus.CREATED);

        /////////////////////////////
        //return ResponseEntity.ok(new AuthResponse(generateToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            // استخرج تاريخ الانتهاء من التوكن
            Date expiration = jwtUtil.extractExpiration(token);

            // أضف التوكن لل blacklist
            blacklistService.addToBlacklist(token, expiration);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("error", HttpStatus.OK.value());
        response.put("message", "Logout successfully");

        return ResponseEntity.ok(response);
    }


    @PostMapping("/loginOtp")
    public ResponseEntity<?> loginWithOtp(@RequestBody AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        CustomUserDetails customUserDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(request.getUsername());
        /*  otp */
        String otp = String.valueOf((int) (Math.random() * 900000) + 100000);
        otpService.sendWhatsappOtp(customUserDetails.getPhoneNumber(), otp);
        /*  otp */
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("error",200);
        result.put("message", "Send OTP To user");

        return ResponseEntity.ok(result);
    }

    @PostMapping("/loginOtpVerify")
    public ResponseEntity<?> loginWithOtpVerify(@RequestBody AuthRequestWithOtp request) {


        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        CustomUserDetails customUserDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(request.getUsername());
       if ( otpService.verifyOtp(customUserDetails.getPhoneNumber(),request.getOtp())){

        String generateToken = jwtUtil.generateToken(customUserDetails.getUsername(), customUserDetails.getUserId(), customUserDetails.getFullName());
        String refreshToken = jwtUtil.generateRefreshToken(customUserDetails);
        // get info user
        UserDtoResponse userDtoResponse =   userService.readById(customUserDetails.getUserId());
           userService.saveLastLogin(customUserDetails.getUserId());
        //
        AuthResponse response = new AuthResponse(
                "Bearer",
                String.valueOf(jwtConfig.getAccessExpiration()),
                generateToken,
                refreshToken
        );


        Map<String, Object> result = new LinkedHashMap<>();
        result.put("token",response);
        result.put("user", userDtoResponse);

        return ResponseEntity.ok(result);}
       else {
           Map<String, Object> result = new LinkedHashMap<>();
           result.put("error",400);
           result.put("message", "Invalid or expired OTP");

           return ResponseEntity.badRequest().body(result);

       }
    }


    @Data
    @AllArgsConstructor
    static class AuthRequest {
        private String username;
        private String password;
    }

    @Data
    @AllArgsConstructor
    static class AuthResponse {
        private String token_type;
        private String expires_in;
        private String access_token;
        private String refresh_token;
      //  private String accessToken;

    }
//
//    @Data
//    @AllArgsConstructor
//    @NoArgsConstructor
//    static class TokenWrapper {
//        private AuthResponse token;
//    }
@Data
@AllArgsConstructor
static class AuthRequestWithOtp {
    private String username;
    private String password;
    private String otp;
}

}