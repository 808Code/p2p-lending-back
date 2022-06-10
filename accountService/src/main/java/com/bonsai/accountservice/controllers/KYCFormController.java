package com.bonsai.accountservice.controllers;

import com.bonsai.accountservice.dto.request.RegisterKYCRequest;
import com.bonsai.accountservice.services.RegistrationService;
import com.bonsai.sharedservice.dtos.response.SuccessResponse;
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

    @PostMapping("/registerKYC")
    public ResponseEntity<SuccessResponse> registerKYC(@ModelAttribute RegisterKYCRequest request){
        registrationService.saveKYC(request);
        return ResponseEntity.ok(
                new SuccessResponse("saved",true)
        );
    }



}
