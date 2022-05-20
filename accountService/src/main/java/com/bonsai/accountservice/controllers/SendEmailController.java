package com.bonsai.accountservice.controllers;

import com.bonsai.accountservice.dto.request.SendEmailRequest;
import com.bonsai.accountservice.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class SendEmailController {

    private final EmailService emailService;


    @PostMapping("/sendEmailOTP")
    public String sendEmailOTP(@RequestBody SendEmailRequest request){
        emailService.sendEmail(request.email());

        return "email has been sent";
    }


}
