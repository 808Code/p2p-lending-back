package com.bonsai.accountservice.dto.request;

public record ResetPasswordRequest(String token,String newPassword) {
}
