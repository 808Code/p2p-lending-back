package com.bonsai.accountservice.services.impl;

import com.bonsai.accountservice.dto.storage.OTP;
import com.bonsai.accountservice.services.OTPStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class LocalOTPStorage implements OTPStorage {

    private final Map<String, OTP> store = new HashMap<>();

    @Override
    public void save(String email,OTP otp) {

        store.put(email, otp);
        log.info("Store {}", store);
    }

    @Override
    public void delete(String email) {
        store.remove(email);
        log.info("Store {}", store);
    }

    @Override
    public Boolean checkIfPresent(String email) {
       return store.containsKey(email);
    }

    @Override
    public OTP getOtp(String email) {
        return store.get(email);
    }



}
