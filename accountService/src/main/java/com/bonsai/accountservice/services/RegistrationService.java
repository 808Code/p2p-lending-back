package com.bonsai.accountservice.services;

import com.bonsai.accountservice.dto.request.CreateBorrowerRequest;
import com.bonsai.accountservice.dto.request.VerifyOTPRequest;
import com.bonsai.accountservice.dto.storage.OTP;

public interface RegistrationService {
    void sendEmailOTP(String email);
    void verifyEmailOTP(VerifyOTPRequest request);
    void saveEmailPassword(CreateBorrowerRequest request);
}
