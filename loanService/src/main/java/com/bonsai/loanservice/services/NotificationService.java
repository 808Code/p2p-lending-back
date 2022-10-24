package com.bonsai.loanservice.services;

import com.bonsai.loanservice.dto.Notification;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

public interface NotificationService {

    SseEmitter addSubscription(String email);
    void pushNotification(Notification notification);
}
