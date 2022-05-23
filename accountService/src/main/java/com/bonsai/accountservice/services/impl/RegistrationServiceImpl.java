package com.bonsai.accountservice.services.impl;

import com.bonsai.accountservice.dto.request.CreateBorrowerRequest;
import com.bonsai.accountservice.dto.storage.OTP;
import com.bonsai.accountservice.exceptions.InvalidOTPException;
import com.bonsai.accountservice.models.UserCredential;
import com.bonsai.accountservice.repositories.UserCredentialRepo;
import com.bonsai.accountservice.services.EmailService;
import com.bonsai.accountservice.services.OTPService;
import com.bonsai.accountservice.services.OTPStorage;
import com.bonsai.accountservice.services.RegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final EmailService emailService;
    private final OTPService otpService;
    private final OTPStorage otpStorage;
    private final UserCredentialRepo userCredentialRepo;

    @Override
    public void sendEmailOTP(String email) {
        String otp = otpService.generateOTP();

        String emailBody = "otpCode is "+otp;
        String subject = "Verify your Email";

        emailService.sendEmail(email, subject, emailBody);

        otpStorage.save(email, OTP.builder().otpCode(otp).verification(false).build());

    }

    @Override
    public void verifyEmailOTP(String email,String otpCode) {
        OTP otpCodeWithVerification = otpStorage.getOtp(email);
        String storedOtp= otpCodeWithVerification.getOtpCode();
        if(storedOtp == null || !storedOtp.equals(otpCode)){
            throw new InvalidOTPException("otpCode not verified");
        }
//        otpStorage.delete(email);
        otpCodeWithVerification.setVerification(true);
        log.info("Changed email {}{}=",email,otpCodeWithVerification );

    }

    @Override
    public void saveEmailPassword(CreateBorrowerRequest request) {
        userCredentialRepo.save(
                UserCredential.builder()
                .email(request.email())
                .password(request.password())
                .build()
        );
    }
}
