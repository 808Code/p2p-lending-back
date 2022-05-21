package com.bonsai.accountservice.services.impl;

import com.bonsai.accountservice.Storage.OTPList;
import com.bonsai.accountservice.services.CheckEmailOTPService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CheckEmailOTPServiceImpl implements CheckEmailOTPService {

    private final OTPList otpList;

    @Override
    public Boolean checkEmailOTP(String email, String OTP) {
    if(otpList.checkKey(email) && otpList.getValue(email).equals(OTP)){
        return true;
    }
    return false;
    }

    @Override
    public void removeOTP(String email) {
        otpList.remove(email);
        otpList.printContent();
    }

}
