package com.bonsai.accountservice.services;

public interface OTPStorage {

    void save(String email, String otp);

    void delete(String email);

    Boolean checkIfPresent(String email);

    String getOtp(String email);
}
