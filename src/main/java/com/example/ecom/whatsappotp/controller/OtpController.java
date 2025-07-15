package com.example.ecom.whatsappotp.controller;


import com.example.ecom.whatsappotp.service.OtpService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/otp")
public class OtpController {

    private final OtpService otpService;

    public OtpController(OtpService otpService) {
        this.otpService = otpService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendOtp(@RequestParam String phoneNumber) {
        System.out.println(phoneNumber);
        String otp = String.valueOf((int) (Math.random() * 900000) + 100000);
        otpService.sendWhatsappOtp(phoneNumber, otp);
        return ResponseEntity.ok("WhatsApp OTP sent to " + phoneNumber);
    }

    @PostMapping("/send/sms")
    public ResponseEntity<String> sendSmsOtp(@RequestParam String phoneNumber) {
        String otp = String.valueOf((int) (Math.random() * 900000) + 100000);
        otpService.sendSmsOtp(phoneNumber, otp);
        return ResponseEntity.ok("SMS OTP sent to " + phoneNumber);
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyOtp(@RequestParam Long phoneNumber, @RequestParam String otp) {
        boolean valid = otpService.verifyOtp(phoneNumber, otp);
        if (valid) {
            return ResponseEntity.ok("✅ OTP is valid");
        } else {
            return ResponseEntity.badRequest().body("❌ Invalid or expired OTP");
        }
    }
}
