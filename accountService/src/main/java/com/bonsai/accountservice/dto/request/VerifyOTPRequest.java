package com.bonsai.accountservice.dto.request;

public record VerifyOTPRequest(String email, String otp) {
}
