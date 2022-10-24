package com.bonsai.loanservice.controllers;

import com.bonsai.loanservice.dto.Notification;
import com.bonsai.loanservice.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@RestController
@RequestMapping("sse")
@RequiredArgsConstructor
@CrossOrigin("*")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/subscribe/{email}")
    public SseEmitter sseEmitter(@PathVariable String email) {
      return notificationService.addSubscription(email);
    }

    @GetMapping("/test")
    public void test() {
         notificationService.pushNotification(new Notification("borrower@gmail.com", "Loan fulfilled"));
    }

}

