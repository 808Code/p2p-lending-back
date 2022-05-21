package com.bonsai.accountservice.services;

public interface CheckEmailOTPService {
    Boolean checkEmailOTP(String email,String OTP);
    public void removeOTP(String email);
}
