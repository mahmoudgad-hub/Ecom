package com.example.ecom.repository;

import com.example.ecom.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByPhoneNumber(String phoneNumber);


    /**
     * تحديث المحاولات الفاشلة فقط (بدون جلب الكائن كاملاً)
     */
    @Modifying
    @Query("UPDATE UserEntity u SET u.failedAttempt = :attempts WHERE u.id = :userId")
    int updateFailedAttempts(@Param("userId") Long userId, @Param("attempts") Integer attempts);

    /**
     * قفل المستخدم (تحديث حقل واحد فقط)
     */
    @Modifying
    @Query("UPDATE UserEntity u SET u.isAccountNonLocked = false WHERE u.id = :userId")
    int lockUser(@Param("userId") Long userId);

    /**
     * إلغاء قفل المستخدم وإعادة تعيين المحاولات
     */
    @Modifying
    @Query("UPDATE UserEntity u SET u.isAccountNonLocked = true, u.failedAttempt = 0 WHERE u.id = :userId")
    int resetFailedAttempts(@Param("userId") Long userId);

    /**
     * جلب معلومات المحاولات فقط (بدون باقي البيانات)
     */
    @Query("SELECT u.id, u.failedAttempt, u.isAccountNonLocked FROM UserEntity u WHERE u.email = :email")
    Optional<Object[]> findAttemptInfoByEmail(@Param("email") String email);

    /**
     * فحص حالة القفل فقط
     */
    @Query("SELECT u.isAccountNonLocked FROM UserEntity u WHERE u.email = :email")
    Optional<Boolean> findLockStatusByEmail(@Param("email") String email);

//    /**
//     * تحديث متعدد للمستخدمين المقفلين (للمهام المجدولة)
//     */
//    @Modifying
//    @Query("UPDATE UserEntity u SET u.isAccountNonLocked = true, u.failedAttempt = 0 WHERE u.isAccountNonLocked = false AND u.lockTime < :unlockTime")
//    int unlockExpiredAccounts(@Param("unlockTime") LocalDateTime unlockTime);

    /**
     * عدد المستخدمين المقفلين (للإحصائيات)
     */
    @Query("SELECT COUNT(u) FROM UserEntity u WHERE u.isAccountNonLocked = false")
    Long countLockedUsers();

}
