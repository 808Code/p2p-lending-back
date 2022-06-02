package com.bonsai.accountservice.controllers;

import com.bonsai.accountservice.dto.request.CreateUserRequest;
import com.bonsai.accountservice.dto.request.VerifyOTPRequest;
import com.bonsai.accountservice.dto.request.SendEmailRequest;

import com.bonsai.accountservice.dto.response.SuccessResponse;

import com.bonsai.accountservice.constants.Roles;
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
       registrationService.verifyEmailOTP(request);

       return ResponseEntity.ok(
               new SuccessResponse("otpCode verified", true)
       );

    }

    @PostMapping("/createBorrower")
    public ResponseEntity<SuccessResponse> createBorrower(@RequestBody CreateUserRequest request){
        registrationService.saveEmailPassword(request, Roles.BORROWER);
        return ResponseEntity.ok(
                new SuccessResponse("Account Created as Borrower.", true)
        );
    }

    @PostMapping("/createInvestor")
    public ResponseEntity<SuccessResponse> createInvestor(@RequestBody CreateUserRequest request){
        registrationService.saveEmailPassword(request,Roles.INVESTOR);
        return ResponseEntity.ok(
                new SuccessResponse("Account Created as Investor.", true)
        );
    }
}
