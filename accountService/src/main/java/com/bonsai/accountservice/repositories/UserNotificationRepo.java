package com.bonsai.accountservice.repositories;

import com.bonsai.accountservice.models.UserNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserNotificationRepo extends JpaRepository<UserNotification, Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM user_notification WHERE user_id = ?1 order by notification_date desc")
    Optional<List<UserNotification>> findAllUserNotification(UUID userId);
}
