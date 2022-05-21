package com.bonsai.accountservice.services;

public interface RegistrationService {
    void sendEmailOTP(String email);
    void verifyEmailOTP(String email, String  otp);
}
