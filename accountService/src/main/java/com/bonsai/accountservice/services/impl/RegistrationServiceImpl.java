package com.bonsai.accountservice.services.impl;

import com.bonsai.accountservice.exceptions.AppException;
import com.bonsai.accountservice.exceptions.InvalidOTPException;
import com.bonsai.accountservice.services.EmailService;
import com.bonsai.accountservice.services.OTPService;
import com.bonsai.accountservice.services.OTPStorage;
import com.bonsai.accountservice.services.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final EmailService emailService;
    private final OTPService otpService;
    private final OTPStorage otpStorage;

    @Override
    public void sendEmailOTP(String email) {
        String otp = otpService.generateOTP();

        String emailBody = "OTP is "+otp;
        String subject = "Verify your Email";

        emailService.sendEmail(email, subject, emailBody);

        otpStorage.save(email, otp);

    }

    @Override
    public void verifyEmailOTP(String email, String otp) {
        String storedOtp = otpStorage.getOtp(email);

        if(storedOtp == null || !storedOtp.equals(otp)){
            throw new InvalidOTPException("OTP not verified");
        }
        otpStorage.delete(email);

    }
}
