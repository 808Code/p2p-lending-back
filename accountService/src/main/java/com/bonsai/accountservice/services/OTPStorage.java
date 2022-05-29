package com.bonsai.accountservice.services;

import com.bonsai.accountservice.dto.storage.OTP;

public interface OTPStorage {

    void save(String email, OTP otp);

    void delete(String email);

    Boolean checkIfPresent(String email);

    OTP getOtp(String email);
}
