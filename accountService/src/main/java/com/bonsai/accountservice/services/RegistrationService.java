package com.bonsai.accountservice.services;

import com.bonsai.accountservice.dto.request.UserCredentialDto;
import com.bonsai.accountservice.dto.request.RegisterKYCRequest;
import com.bonsai.accountservice.dto.request.VerifyOTPRequest;

public interface RegistrationService {
    void sendEmailOTP(String email);
    void verifyEmailOTP(VerifyOTPRequest request);
    void saveEmailPassword(UserCredentialDto request, String role);
    void saveKYC(RegisterKYCRequest request);

}
