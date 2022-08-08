package com.bonsai.accountservice.controllers.registration;

import com.bonsai.accountservice.dto.request.RegisterKYCRequest;
import com.bonsai.accountservice.services.RegistrationService;
import com.bonsai.accountservice.services.StorageService;
import com.bonsai.sharedservice.dtos.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;


@RequiredArgsConstructor
@Controller
@Slf4j
@RequestMapping("/registration")
public class KYCFormController {
    private final RegistrationService registrationService;
    private final StorageService storageService;

    @PostMapping("/registerKYC")
    public ResponseEntity<SuccessResponse> registerKYC(@ModelAttribute RegisterKYCRequest request){
        registrationService.saveKYC(request);
        return ResponseEntity.ok(
                new SuccessResponse("saved",true)
        );
    }

    @GetMapping(value = "/documents/{fileName}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> getDocument(@PathVariable String fileName) throws IOException {
        return ResponseEntity.ok(storageService.retrieve(fileName));
    }

}
