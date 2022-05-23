package com.bonsai.accountservice.services;

import com.bonsai.accountservice.dto.request.CreateBorrowerRequest;
import com.bonsai.accountservice.dto.storage.OTP;

public interface RegistrationService {
    void sendEmailOTP(String email);
    void verifyEmailOTP(String email,String otpCode);
    void saveEmailPassword(CreateBorrowerRequest request);
}
