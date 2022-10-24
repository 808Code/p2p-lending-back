package com.bonsai.loanservice.services.impl;

import com.bonsai.loanservice.dto.Notification;
import com.bonsai.loanservice.services.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    private final Map<String, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

    @Override
    public SseEmitter addSubscription(String email) {
        log.info("Subscribing {}",email);
        SseEmitter sseEmitter = new SseEmitter(60 * 60 * 24 * 1000L);
        sseEmitters.put(email, sseEmitter);

        log.info("Subscribed {}",email);
        return sseEmitter;
    }

    @Override
    public void pushNotification(Notification notification) {

        log.info("Size "+sseEmitters.values().size());
        for (var entry : sseEmitters.entrySet()) {
            log.info("Entry key {} {}", entry.getKey(), notification.email());
            if (entry.getKey().equals(notification.email())) {
                log.info("sending....");
               try{
                   entry.getValue().send(
                           SseEmitter.event()
                                   .data(notification)
                   );
               }catch (Exception e){
                   e.printStackTrace();
               }
            }
        }
    }
}
