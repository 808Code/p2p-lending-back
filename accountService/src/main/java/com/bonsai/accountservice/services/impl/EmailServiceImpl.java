package com.bonsai.accountservice.services.impl;

import com.bonsai.accountservice.services.EmailService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    @Override
    public void sendEmail(String to) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("your_email_here");
        message.setTo(to);
        message.setSubject("p2p lending");
        message.setText("Here is otp");
        //generate
        javaMailSender.send(message);
    }
}
