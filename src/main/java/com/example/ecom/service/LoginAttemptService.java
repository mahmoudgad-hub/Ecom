package com.example.ecom.service;

import com.example.ecom.entity.UserEntity;
import com.example.ecom.repository.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class  LoginAttemptService {

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private UserRepo userRepo;

    // Cache في الذاكرة للمحاولات الفاشلة (تجنب استدعاءات DB متكررة)
    private final Map<String, AttemptInfo> attemptCache = new ConcurrentHashMap<>();

    // Cache للتكوينات
    private Integer maxFailedAttempts;

    // معلومات المحاولة
    private static class AttemptInfo {
        int failedAttempts;
        boolean isLocked;
        long lastAttemptTime;

        AttemptInfo(int failedAttempts, boolean isLocked) {
            this.failedAttempts = failedAttempts;
            this.isLocked = isLocked;
            this.lastAttemptTime = System.currentTimeMillis();
        }
    }

    /**
     * فحص حالة قفل الحساب بدون استدعاءات DB متكررة
     */
    public String checkStatusAccountLock(String username) {
        try {
            // تحقق من الـ cache أولاً
            AttemptInfo cachedInfo = attemptCache.get(username);
            if (cachedInfo != null && cachedInfo.isLocked) {
                return "423"; // مقفل
            }

            // إذا لم توجد معلومات في الـ cache، جلب من DB مرة واحدة فقط
            UserEntity user = findUserByUsername(username);
            if (user == null) {
                return "404"; // مستخدم غير موجود
            }

            // تحديث الـ cache
            updateAttemptCache(username, user);

            return Boolean.TRUE.equals(user.getIsAccountNonLocked()) ? "200" : "423";

        } catch (Exception e) {
            log.error("خطأ في فحص حالة قفل الحساب للمستخدم: {}", username, e);
            return "500"; // خطأ في النظام
        }
    }

    /**
     * فحص محاولة المستخدم مع تحسين الأداء
     */
    public ResponseEntity<?> checkUserAttempt(String username) {
        try {
            // جلب المستخدم مرة واحدة فقط
            UserEntity user = findUserByUsername(username);
            if (user == null) {
                return createErrorResponse("400", "Invalid username or password");
            }

            // تحديث الـ cache
            updateAttemptCache(username, user);

            // التحقق من حالة القفل
            if (!Boolean.TRUE.equals(user.getIsAccountNonLocked())) {
                return createErrorResponse("423", "Your account is locked. Try again later.");
            }

            int maxAttempts = getMaxFailedAttempts();
            int currentAttempts = (user.getFailedAttempt() == null) ? 0 : user.getFailedAttempt();

            if (currentAttempts < maxAttempts) {
                // زيادة المحاولات بشكل async
                increaseFailedAttemptsAsync(user, username);
                return createErrorResponse("400", "Invalid username or password");
            } else {
                // قفل الحساب بشكل async
                lockUserAsync(user, username);
                return createErrorResponse("423", "User account is locked due to too many failed login attempts.");
            }

        } catch (Exception e) {
            log.error("خطأ في فحص محاولة المستخدم: {}", username, e);
            return createErrorResponse("500", "Internal server error");
        }
    }

    /**
     * إعادة تعيين المحاولات الفاشلة بشكل async
     */
    @Async
    public CompletableFuture<Void> resetFailedAttempts(String username) {
        try {
            // إزالة من الـ cache أولاً
            attemptCache.remove(username);

            // تحديث قاعدة البيانات إذا لزم الأمر
            UserEntity user = findUserByUsername(username);
            if (user != null && shouldResetAttempts(user)) {
                resetUserAttempts(user);
                log.debug("تم إعادة تعيين المحاولات الفاشلة للمستخدم: {}", username);
            }

            return CompletableFuture.completedFuture(null);

        } catch (Exception e) {
            log.error("خطأ في إعادة تعيين المحاولات الفاشلة للمستخدم: {}", username, e);
            return CompletableFuture.failedFuture(e);
        }
    }

    /**
     * زيادة المحاولات الفاشلة بشكل async
     */
    @Async
    @Transactional
    protected void increaseFailedAttemptsAsync(UserEntity user, String username) {
        try {
            int currentAttempts = (user.getFailedAttempt() == null) ? 0 : user.getFailedAttempt();
            int newFailAttempts = currentAttempts + 1;

            // تحديث الـ cache فوراً
            AttemptInfo attemptInfo = new AttemptInfo(newFailAttempts, false);
            attemptCache.put(username, attemptInfo);

            // تحديث قاعدة البيانات
            userRepo.updateFailedAttempts(user.getUserId(), newFailAttempts);

            // فحص إذا كان يحتاج قفل
            if (newFailAttempts >= getMaxFailedAttempts()) {
                lockUserAsync(user, username);
            }

            log.debug("زيادة المحاولات الفاشلة للمستخدم: {} إلى: {}", username, newFailAttempts);

        } catch (Exception e) {
            log.error("خطأ في زيادة المحاولات الفاشلة للمستخدم: {}", username, e);
        }
    }

    /**
     * قفل المستخدم بشكل async
     */
    @Async
    @Transactional
    protected void lockUserAsync(UserEntity user, String username) {
        try {
            // تحديث الـ cache فوراً
            AttemptInfo attemptInfo = new AttemptInfo(getMaxFailedAttempts(), true);
            attemptCache.put(username, attemptInfo);

            // تحديث قاعدة البيانات
            userRepo.lockUser(user.getUserId());

            log.warn("تم قفل الحساب للمستخدم: {}", username);

        } catch (Exception e) {
            log.error("خطأ في قفل المستخدم: {}", username, e);
        }
    }

    /**
     * جلب المستخدم بالـ username (مع cache محتمل)
     */
    private UserEntity findUserByUsername(String username) {
        return userRepo.findByEmail(username).orElse(null);
    }

    /**
     * تحديث cache المحاولات
     */
    private void updateAttemptCache(String username, UserEntity user) {
        int attempts = (user.getFailedAttempt() == null) ? 0 : user.getFailedAttempt();
        boolean isLocked = !Boolean.TRUE.equals(user.getIsAccountNonLocked());
        attemptCache.put(username, new AttemptInfo(attempts, isLocked));
    }

    /**
     * الحصول على الحد الأقصى للمحاولات الفاشلة مع cache
     */
    @Cacheable(value = "configCache", key = "'MAX_FAILED_ATTEMPTS'")
    private Integer getMaxFailedAttempts() {
        if (maxFailedAttempts == null) {
            try {
                maxFailedAttempts = Integer.parseInt(configurationService.getConfigValue("MAX_FAILED_ATTEMPTS"));
            } catch (NumberFormatException e) {
                log.warn("قيمة MAX_FAILED_ATTEMPTS غير صحيحة، استخدام القيمة الافتراضية: 3");
                maxFailedAttempts = 3; // قيمة افتراضية
            }
        }
        return maxFailedAttempts;
    }

    /**
     * فحص إذا كان المستخدم يحتاج إعادة تعيين المحاولات
     */
    private boolean shouldResetAttempts(UserEntity user) {
        return user.getFailedAttempt() != null && user.getFailedAttempt() > 0;
    }

    /**
     * إعادة تعيين محاولات المستخدم
     */
    @Transactional
    private void resetUserAttempts(UserEntity user) {
        userRepo.resetFailedAttempts(user.getUserId());
    }

    /**
     * إنشاء response للخطأ
     */
    private ResponseEntity<?> createErrorResponse(String status, String message) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", status);
        response.put("message", message);

        HttpStatus httpStatus = switch (status) {
            case "400" -> HttpStatus.BAD_REQUEST;
            case "423" -> HttpStatus.LOCKED;
            case "404" -> HttpStatus.NOT_FOUND;
            case "500" -> HttpStatus.INTERNAL_SERVER_ERROR;
            default -> HttpStatus.BAD_REQUEST;
        };

        return ResponseEntity.status(httpStatus).body(response);
    }

    /**
     * مسح الـ cache (للصيانة)
     */
    public void clearAttemptCache() {
        attemptCache.clear();
        maxFailedAttempts = null;
        log.info("تم مسح cache المحاولات الفاشلة");
    }

    /**
     * الحصول على معلومات المحاولات من الـ cache
     */
    public AttemptInfo getAttemptInfo(String username) {
        return attemptCache.get(username);
    }
}