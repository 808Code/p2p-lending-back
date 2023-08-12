package com.bonsai.loanservice.services;

import com.bonsai.accountservice.models.UserCredential;
import com.bonsai.loanservice.dto.Notification;
import com.bonsai.loanservice.dto.UserNotificationResponse;
import com.bonsai.sharedservice.enums.NotificationType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

public interface NotificationService {

    SseEmitter addSubscription(String email);
    void pushNotification(Notification notification);
    void saveNotification(UserCredential userCredential, NotificationType notificationType, String message);

    List<UserNotificationResponse> getUserNotification(String email);
}
