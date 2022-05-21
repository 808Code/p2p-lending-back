package com.bonsai.accountservice.controllers;

import com.bonsai.accountservice.dto.request.VerifyOTPRequest;
import com.bonsai.accountservice.dto.request.SendEmailRequest;

import com.bonsai.accountservice.dto.response.SuccessResponse;

import com.bonsai.accountservice.services.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping("/sendEmailOTP")
    public ResponseEntity<SuccessResponse> sendEmailOTP(@RequestBody SendEmailRequest request){
        registrationService.sendEmailOTP(request.email());

        return ResponseEntity.ok(
                new SuccessResponse("An Otp has been Sent", true)
        );
    }

    @PostMapping("/verifyEmailOTP")
    public ResponseEntity<SuccessResponse> verifyEmailOTP(@RequestBody VerifyOTPRequest request){
       registrationService.verifyEmailOTP(request.email(), request.otp());

       return ResponseEntity.ok(
               new SuccessResponse("OTP verified", true)
       );

    }





}
