package com.bonsai.accountservice.services.impl;

import com.bonsai.accountservice.Storage.OTPList;
import com.bonsai.accountservice.services.EmailService;
import com.bonsai.accountservice.services.OtpGenerateService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private final OtpGenerateService otpGenerateService;
    private final OTPList otpList;
    @Override
    public void sendEmail(String to) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("your_email_here");
        message.setTo(to);
        message.setSubject("p2p lending");
        String otp=otpGenerateService.generateOTP();

        otpList.add(to,otp);
        otpList.printContent();
        message.setText("Your Otp ="+otp);
        //generate
        javaMailSender.send(message);
    }
}
