package com.bonsai.accountservice.controllers;


import com.bonsai.accountservice.dto.request.GetKYCRequest;
import com.bonsai.accountservice.dto.request.RegisterKYCRequest;
import com.bonsai.accountservice.dto.request.VerifyKYCRequest;
import com.bonsai.accountservice.dto.response.SuccessResponse;
import com.bonsai.accountservice.services.KYCService;
import com.bonsai.accountservice.services.RegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;



@RequiredArgsConstructor
@Controller
@Slf4j
public class KYCFormController {
    private final RegistrationService registrationService;
    private final KYCService kycService;


    @PostMapping("/registerKYC")
    public ResponseEntity<SuccessResponse> registerKYC(@ModelAttribute RegisterKYCRequest request){
        log.info("Request = {}",request);
        registrationService.saveKYC(request);
        return ResponseEntity.ok(
                new SuccessResponse("saved",true)
        );
    }



}
