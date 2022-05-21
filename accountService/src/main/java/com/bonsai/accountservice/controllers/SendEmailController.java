package com.bonsai.accountservice.controllers;

import com.bonsai.accountservice.dto.request.CheckEmailRequest;
import com.bonsai.accountservice.dto.request.SendEmailRequest;

import com.bonsai.accountservice.services.CheckEmailOTPService;
import com.bonsai.accountservice.services.EmailService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class SendEmailController {

    private final EmailService emailService;
    private final CheckEmailOTPService checkEmailOTPService;


    @PostMapping("/sendEmailOTP")
    public String sendEmailOTP(@RequestBody SendEmailRequest request){
        emailService.sendEmail(request.email());

        return "An Otp has been Sent";
    }

    @PostMapping("/verifyEmailOTP")
    public String verifyEmailOTP(@RequestBody CheckEmailRequest request){
        if(checkEmailOTPService.checkEmailOTP(request.email(), request.otp())){
            checkEmailOTPService.removeOTP(request.email());
            return "OTP Verified";
        }

        return "Wrong OTP";
    }





}
