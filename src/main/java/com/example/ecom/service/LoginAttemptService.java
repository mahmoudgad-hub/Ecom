package com.example.ecom.service;

import com.example.ecom.entity.UserEntity;
import com.example.ecom.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class LoginAttemptService {

    @Autowired
    private ConfigurationService configurationService;

   // private static final int MAX_FAILED_ATTEMPTS = 3;
    private static final long LOCK_TIME_DURATION = 15; // بالـ minutes


    @Autowired
    private UserRepo  userRepo;


    public void increaseFailedAttempts(UserEntity user) {
        int currentAttempts = (user.getFailedAttempt() == null) ? 0 : user.getFailedAttempt();
        int newFailAttempts = currentAttempts + 1;
        user.setFailedAttempt(newFailAttempts);
        if (newFailAttempts >= Integer.parseInt(configurationService.getConfigValue("MAX_FAILED_ATTEMPTS")) //MAX_FAILED_ATTEMPTS
        ) {
            lock(user);
        }
        userRepo.save(user);
    }

    public String checkStatusAccountLock(String username) {
        UserEntity user = userRepo.findByEmail(username).orElse(null);

        if (user == null) {
            return "404";
        }

        if (!user.getIsAccountNonLocked()) {
            return "423";
        }
        return "200";
    }

    public void resetFailedAttempts(String username) {
        UserEntity user = userRepo.findByEmail(username).orElse(null);

        if (user != null && ((user.getFailedAttempt() == null) ? 0 : user.getFailedAttempt()) > 0) {
            user.setFailedAttempt(0);
            user.setIsAccountNonLocked(true);
           // user.setLockTime(null);
            userRepo.save(user);
        }
    }

    private void lock(UserEntity user) {
        System.out.println("lock");
        user.setIsAccountNonLocked(false);

       // user.setLockTime(LocalDateTime.now());
        userRepo.save(user);
    }

//    public boolean unlockWhenTimeExpired(UserEntity user) {
//        if (user.getLockTime() == null) return false;
//
//        LocalDateTime lockTime = user.getLockTime();
//        LocalDateTime now = LocalDateTime.now();
//
//        if (lockTime.plusMinutes(LOCK_TIME_DURATION).isBefore(now)) {
//            user.setAccountNonLocked(true);
//            user.setLockTime(null);
//            user.setFailedAttempt(0);
//            userRepository.save(user);
//            return true;
//        }
//        return false;
//    }

    public   ResponseEntity<?>  checkUserAttempt (String username ){

    UserEntity user = userRepo.findByEmail(username).orElse(null);
    if (user != null) {
       // if (user.getIsAccountNonLocked()) {
        System.out.println(user.getIsAccountNonLocked());
        if(Boolean.TRUE.equals(user.getIsAccountNonLocked())){
            if (user.getFailedAttempt() <  Integer.parseInt(configurationService.getConfigValue("MAX_FAILED_ATTEMPTS"))//LoginAttemptService.MAX_FAILED_ATTEMPTS
            ) {
                increaseFailedAttempts(user);
            } else {
                lock(user);
                Map<String, Object> responseLock = new LinkedHashMap<>();
                responseLock.put("status","423");
                responseLock.put("message","User account is locked due to too many failed login attempts.");

                return ResponseEntity.status(HttpStatus.LOCKED).body(responseLock);

            }
        } else {
//            if (unlockWhenTimeExpired(user)) {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                        .body("Your account was unlocked. Please try again.");
//            }
            Map<String, Object> responseLock = new LinkedHashMap<>();
            responseLock.put("status","423");
            responseLock.put("message","Your account is locked. Try again later.");

            return ResponseEntity.status(HttpStatus.LOCKED).body(responseLock);

        }
    }
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status","400");
        response.put("message","Invalid username or password");

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);


}

}
