package com.bonsai.loanservice.services.impl;

import com.bonsai.accountservice.models.UserCredential;
import com.bonsai.accountservice.models.UserNotification;
import com.bonsai.accountservice.repositories.UserCredentialRepo;
import com.bonsai.accountservice.repositories.UserNotificationRepo;
import com.bonsai.loanservice.dto.Notification;
import com.bonsai.loanservice.dto.UserNotificationResponse;
import com.bonsai.loanservice.services.NotificationService;
import com.bonsai.sharedservice.enums.NotificationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final Map<String, SseEmitter> sseEmitters = new ConcurrentHashMap<>();
    private final UserNotificationRepo userNotificationRepo;
    private final UserCredentialRepo userCredentialRepo;

    @Override
    public SseEmitter addSubscription(String email) {
        log.info("Subscribing {}", email);
        SseEmitter sseEmitter = new SseEmitter(60 * 60 * 24 * 1000L);
        sseEmitters.put(email, sseEmitter);

        log.info("Subscribed {}", email);
        return sseEmitter;
    }

    @Override
    public void pushNotification(Notification notification) {

        log.info("Size " + sseEmitters.values().size());
        for (var entry : sseEmitters.entrySet()) {
            log.info("Entry key {} {}", entry.getKey(), notification.email());
            if (entry.getKey().equals(notification.email())) {
                log.info("sending....");
                try {
                    entry.getValue().send(
                            SseEmitter.event()
                                    .data(notification)
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void saveNotification(UserCredential userCredential, NotificationType notificationType, String message) {
        UserNotification userNotification = UserNotification.builder()
                .user(userCredential)
                .notificationType(notificationType)
                .notificationMessage(message)
                .notificationDate(LocalDateTime.now())
                .build();
        userNotificationRepo.save(userNotification);
    }

    @Override
    public List<UserNotificationResponse> getUserNotification(String email) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd 'at' HH:mm");
        UserCredential userCredential = userCredentialRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        List<UserNotification> userNotifications = userNotificationRepo.findAllUserNotification(userCredential.getId()).orElse(List.of());
        return userNotifications.stream().map(userNotification -> UserNotificationResponse.builder()
                .notificationDate(userNotification.getNotificationDate().format(formatter))
                .notificationMessage(userNotification.getNotificationMessage())
                .notificationType(userNotification.getNotificationType().name())
                .build()).toList();
    }


}
