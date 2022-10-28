package com.bonsai.accountservice.controllers.registration;

import com.bonsai.accountservice.dto.request.*;
import com.bonsai.accountservice.constants.Roles;
import com.bonsai.accountservice.models.KYC;
import com.bonsai.accountservice.services.KYCService;
import com.bonsai.accountservice.dto.request.UserAuth;
import com.bonsai.accountservice.dto.request.VerifyOTPRequest;
import com.bonsai.accountservice.dto.request.SendEmailRequest;
import com.bonsai.accountservice.services.RegistrationService;
import com.bonsai.sharedservice.dtos.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/registration")
public class RegistrationController {

    private final RegistrationService registrationService;
    private final KYCService kycService;

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
    public ResponseEntity<SuccessResponse> createBorrower(@RequestBody UserAuth request){
        registrationService.saveEmailPassword(request, Roles.BORROWER);
        return ResponseEntity.ok(
                new SuccessResponse("Account Created as Borrower.", true)
        );
    }

    @PostMapping("/createLender")
    public ResponseEntity<SuccessResponse> createLENDER(@RequestBody UserAuth request){
        registrationService.saveEmailPassword(request,Roles.LENDER);
        return ResponseEntity.ok(
                new SuccessResponse("Account Created as Lender.", true)
        );
    }

    @GetMapping("/verifyKYC/{email}")
    public ResponseEntity<SuccessResponse> verifyKYC(@PathVariable String email){
        kycService.verifyKYC(email);
        return ResponseEntity.ok(
                new SuccessResponse("KYC verified.",true)
        );
    }

    @PostMapping("/getKYC")
    public ResponseEntity<SuccessResponse> getKYC(@RequestBody GetKYCRequest request){
        KYC kyc = kycService.getKYC(request);
        return ResponseEntity.ok(
                new SuccessResponse("KYC for "+request.email(),kyc)
        );
    }

}
