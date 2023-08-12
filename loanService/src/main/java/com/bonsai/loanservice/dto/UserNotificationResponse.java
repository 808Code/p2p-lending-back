package com.bonsai.loanservice.dto;

import lombok.Builder;

@Builder
public record UserNotificationResponse(String message, String notificationType,
                                       String notificationDate,String notificationMessage) {
}
