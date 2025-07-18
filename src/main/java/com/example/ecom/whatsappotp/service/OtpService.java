package com.example.ecom.whatsappotp.service;


import com.example.ecom.entity.UserEntity;
import com.example.ecom.exception.user.UserExistException;
import com.example.ecom.repository.UserRepo;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OtpService {

    @Value("${twilio.whatsapp.from}")
    private String fromNumber;

    @Autowired
    private final UserRepo userRepo;

    public OtpService(UserRepo userRepo) {

        this.userRepo = userRepo;
    }

    public void sendWhatsappOtp(String toNumber, String otp) {

        Message.creator(
                new PhoneNumber("whatsapp:+" + toNumber),
                new PhoneNumber(fromNumber),
                "Your OTP code is: " + otp
        ).create();
System.out.println(toNumber+"        "+otp);
        saveOtp(toNumber, otp);
    }

    public void sendSmsOtp(String toNumber, String otp) {
        Message.creator(
                new PhoneNumber(toNumber),
                new PhoneNumber(fromNumber.replace("whatsapp:", "")),
                "Your OTP code is: " + otp
        ).create();

        saveOtp(toNumber, otp);
    }

    private void saveOtp(String toNumber, String otp) {

        Optional<UserEntity> userEntity= userRepo.findByPhoneNumber(toNumber);
        if(userEntity.isEmpty()){
            //TODO: add exception handler
            throw new UserExistException("No found for this user ID");
        }else{
            UserEntity userEnt = userEntity.get();
            userEnt.setOtpCode(otp);
            userEnt.setCreatedAt(LocalDateTime.now());
            userEnt.setExpiresAt(LocalDateTime.now().plusMinutes(2));

            userRepo.save(userEnt);

        }




    }

    public boolean verifyOtp(Long phoneNumber, String otp) {
        return userRepo.findById(phoneNumber)
                .filter(o -> o.getOtpCode().equals(otp))
                .filter(o -> o.getExpiresAt().isAfter(LocalDateTime.now()))
                .isPresent();
    }
}

