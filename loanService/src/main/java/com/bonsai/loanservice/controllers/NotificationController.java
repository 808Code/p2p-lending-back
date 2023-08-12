package com.bonsai.loanservice.controllers;

import com.bonsai.loanservice.services.NotificationService;
import com.bonsai.sharedservice.dtos.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
@CrossOrigin("*")
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/subscribe/{email}")
    public SseEmitter sseEmitter(@PathVariable String email) {
      return notificationService.addSubscription(email);
    }
    @GetMapping("/getUserNotification")
    public ResponseEntity<SuccessResponse> getUserNotification(Authentication authentication) {
        String email = authentication.getPrincipal().toString();
        return ResponseEntity.ok(new SuccessResponse("success", notificationService.getUserNotification(email)));
    }
}

