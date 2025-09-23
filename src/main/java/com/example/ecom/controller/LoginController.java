package com.example.ecom.controller;

import com.example.ecom.Lang.MessageUtil;
import com.example.ecom.auth.CustomUserDetails;
import com.example.ecom.auth.config.JwtConfig;
import com.example.ecom.auth.config.JwtUtil;
import com.example.ecom.dto.ApiResponseDto;
import com.example.ecom.dto.UserDtoResponse;
import com.example.ecom.service.CustomUserDetailsService;
import com.example.ecom.service.LoginAttemptService;
import com.example.ecom.service.TokenBlacklistService;
import com.example.ecom.service.UserService;
import com.example.ecom.whatsappotp.service.OtpService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/auth")
@Slf4j
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
    private OtpService otpService;

    @Autowired
    private LoginAttemptService loginAttemptService;

    @Autowired
    private MessageUtil messageUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        long startTime = System.currentTimeMillis();
        log.debug("بدء عملية تسجيل الدخول للمستخدم: {}", request.getUsername());

        try {
            // 1. فحص حالة قفل الحساب (سريع - cache)
            String lockStatus = loginAttemptService.checkStatusAccountLock(request.getUsername());
            if ("423".equals(lockStatus)) {
                log.warn("محاولة دخول لحساب مقفل: {}", request.getUsername());
                return ResponseEntity.status(HttpStatus.LOCKED)
                        .body(new ApiResponseDto<>(423, messageUtil.get("sys.account_Locked"), null));
            }

            // 2. المصادقة (أبطأ جزء - لكن ضروري)
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            // 3. إعادة تعيين المحاولات الفاشلة (async)
            loginAttemptService.resetFailedAttempts(request.getUsername());

            // 4. تحميل تفاصيل المستخدم مرة واحدة فقط
            CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(request.getUsername());

            // 5. إنشاء التوكنز (سريع - عملية حسابية)
            String accessToken = jwtUtil.generateToken(
                    userDetails.getUsername(),
                    userDetails.getUserId(),
                    userDetails.getFullName()
            );
            String refreshToken = jwtUtil.generateRefreshToken(userDetails);

            // 6. جلب بيانات المستخدم وحفظ آخر تسجيل دخول بشكل متوازي
            CompletableFuture<UserDtoResponse> userDataFuture = getUserDataAsync(userDetails.getUserId());
            CompletableFuture<Void> lastLoginFuture = saveLastLoginAsync(userDetails.getUserId());

            // انتظار انتهاء العمليات المتوازية
            CompletableFuture<Void> allOperations = CompletableFuture.allOf(userDataFuture, lastLoginFuture);

            try {
                allOperations.get(); // انتظار لمدة معقولة
                UserDtoResponse userResponse = userDataFuture.get();

                // 7. إعداد الاستجابة
                AuthResponse authResponse = new AuthResponse(
                        "Bearer",
                        String.valueOf(jwtConfig.getAccessExpiration()),
                        accessToken,
                        refreshToken
                );

                Map<String, Object> result = new LinkedHashMap<>();
                result.put("token", authResponse);
                result.put("user", userResponse);

                long duration = System.currentTimeMillis() - startTime;
                log.info("تم تسجيل الدخول بنجاح للمستخدم: {} في {}ms", request.getUsername(), duration);

                return ResponseEntity.ok(result);

            } catch (Exception e) {
                log.warn("تأخر في العمليات المساعدة، سيتم إرجاع الاستجابة بدون انتظار: {}", e.getMessage());

                // في حالة تأخر العمليات المساعدة، أرجع الاستجابة بدون انتظار
                AuthResponse authResponse = new AuthResponse(
                        "Bearer",
                        String.valueOf(jwtConfig.getAccessExpiration()),
                        accessToken,
                        refreshToken
                );

                // جلب البيانات بشكل متزامن كـ fallback
                UserDtoResponse userResponse = userService.readById(userDetails.getUserId());

                Map<String, Object> result = new LinkedHashMap<>();
                result.put("token", authResponse);
                result.put("user", userResponse);

                return ResponseEntity.ok(result);
            }

        } catch (BadCredentialsException ex) {
            log.warn("بيانات اعتماد خاطئة للمستخدم: {}", request.getUsername());
            return loginAttemptService.checkUserAttempt(request.getUsername());
        } catch (Exception e) {
            log.error("خطأ غير متوقع في تسجيل الدخول للمستخدم: {}", request.getUsername(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDto<>(500, "خطأ في النظام", null));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        try {
            String header = request.getHeader("Authorization");
            if (header != null && header.startsWith("Bearer ")) {
                String token = header.substring(7);

                // إضافة التوكن للـ blacklist بشكل async
                addTokenToBlacklistAsync(token);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("error", HttpStatus.OK.value());
            response.put("message", "Logout successfully");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("خطأ في تسجيل الخروج", e);
            return ResponseEntity.ok(Map.of("error", 200, "message", "Logout completed"));
        }
    }

    @PostMapping("/loginOtp")
    public ResponseEntity<?> loginWithOtp(@RequestBody AuthRequest request) {
        long startTime = System.currentTimeMillis();

        try {
            // المصادقة
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(request.getUsername());

            // إنشاء وإرسال OTP بشكل async
            sendOtpAsync(userDetails.getPhoneNumber());

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("error", 200);
            result.put("message", "Send OTP To user");

            long duration = System.currentTimeMillis() - startTime;
            log.info("تم إرسال OTP للمستخدم: {} في {}ms", request.getUsername(), duration);

            return ResponseEntity.ok(result);

        } catch (BadCredentialsException ex) {
            return loginAttemptService.checkUserAttempt(request.getUsername());
        } catch (Exception e) {
            log.error("خطأ في إرسال OTP للمستخدم: {}", request.getUsername(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", 500, "message", "خطأ في إرسال OTP"));
        }
    }

    @PostMapping("/loginOtpVerify")
    public ResponseEntity<?> loginWithOtpVerify(@RequestBody AuthRequestWithOtp request) {
        long startTime = System.currentTimeMillis();

        try {
            // المصادقة
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(request.getUsername());

            // التحقق من OTP
            if (!otpService.verifyOtp(userDetails.getPhoneNumber(), request.getOtp())) {
                Map<String, Object> result = new LinkedHashMap<>();
                result.put("error", 400);
                result.put("message", "Invalid or expired OTP");
                return ResponseEntity.badRequest().body(result);
            }

            // إنشاء التوكنز
            String accessToken = jwtUtil.generateToken(
                    userDetails.getUsername(),
                    userDetails.getUserId(),
                    userDetails.getFullName()
            );
            String refreshToken = jwtUtil.generateRefreshToken(userDetails);

            // العمليات المساعدة بشكل async
            CompletableFuture<UserDtoResponse> userDataFuture = getUserDataAsync(userDetails.getUserId());
            CompletableFuture<Void> lastLoginFuture = saveLastLoginAsync(userDetails.getUserId());

            // إعداد الاستجابة
            AuthResponse authResponse = new AuthResponse(
                    "Bearer",
                    String.valueOf(jwtConfig.getAccessExpiration()),
                    accessToken,
                    refreshToken
            );

            try {
                UserDtoResponse userResponse = userDataFuture.get();
                lastLoginFuture.get(); // انتظار حفظ آخر تسجيل دخول

                Map<String, Object> result = new LinkedHashMap<>();
                result.put("token", authResponse);
                result.put("user", userResponse);

                long duration = System.currentTimeMillis() - startTime;
                log.info("تم تسجيل الدخول بـ OTP للمستخدم: {} في {}ms", request.getUsername(), duration);

                return ResponseEntity.ok(result);

            } catch (Exception e) {
                // fallback في حالة تأخر العمليات المساعدة
                UserDtoResponse userResponse = userService.readById(userDetails.getUserId());

                Map<String, Object> result = new LinkedHashMap<>();
                result.put("token", authResponse);
                result.put("user", userResponse);

                return ResponseEntity.ok(result);
            }

        } catch (BadCredentialsException ex) {
            return loginAttemptService.checkUserAttempt(request.getUsername());
        } catch (Exception e) {
            log.error("خطأ في التحقق من OTP للمستخدم: {}", request.getUsername(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", 500, "message", "خطأ في التحقق من OTP"));
        }
    }

    // العمليات المساعدة Async
    @Async
    private CompletableFuture<UserDtoResponse> getUserDataAsync(Long userId) {
        try {
            UserDtoResponse userResponse = userService.readById(userId);
            return CompletableFuture.completedFuture(userResponse);
        } catch (Exception e) {
            log.error("خطأ في جلب بيانات المستخدم async: {}", userId, e);
            return CompletableFuture.failedFuture(e);
        }
    }

    @Async
    private CompletableFuture<Void> saveLastLoginAsync(Long userId) {
        try {
            userService.saveLastLogin(userId);
            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            log.error("خطأ في حفظ آخر تسجيل دخول async: {}", userId, e);
            return CompletableFuture.failedFuture(e);
        }
    }

    @Async
    private void sendOtpAsync(String phoneNumber) {
        try {
            String otp = String.valueOf((int) (Math.random() * 900000) + 100000);
            otpService.sendWhatsappOtp(phoneNumber, otp);
        } catch (Exception e) {
            log.error("خطأ في إرسال OTP async: {}", phoneNumber, e);
        }
    }

    @Async
    private void addTokenToBlacklistAsync(String token) {
        try {
            Date expiration = jwtUtil.extractExpiration(token);
            blacklistService.addToBlacklist(token, expiration);
        } catch (Exception e) {
            log.error("خطأ في إضافة التوكن للـ blacklist async: {}", token, e);
        }
    }

    // DTOs
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
    }

    @Data
    @AllArgsConstructor
    static class AuthRequestWithOtp {
        private String username;
        private String password;
        private String otp;
    }
}