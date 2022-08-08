package com.bonsai.accountservice.services;

import com.bonsai.accountservice.dto.request.UserAuth;
import com.bonsai.accountservice.dto.request.RegisterKYCRequest;
import com.bonsai.accountservice.dto.request.VerifyOTPRequest;

import javax.transaction.Transactional;

public interface RegistrationService {
    void sendEmailOTP(String email);
    void verifyEmailOTP(VerifyOTPRequest request);

    @Transactional
    void saveEmailPassword(UserAuth request, String role);
    void saveKYC(RegisterKYCRequest request);

}
