package com.bonsai.accountservice.services.impl;

import com.bonsai.accountservice.dto.request.GetKYCRequest;
import com.bonsai.accountservice.dto.request.VerifyKYCRequest;
import com.bonsai.accountservice.models.UserCredential;
import com.bonsai.accountservice.repositories.UserCredentialRepo;
import com.bonsai.accountservice.services.KYCService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;



@Service
@Slf4j
@RequiredArgsConstructor
public class KYCServiceImpl implements KYCService {
    private final UserCredentialRepo userCredentialRepo;

    @Override
    public void getKYC(GetKYCRequest request) {

    }

    @Override
    public void verifyKYC(VerifyKYCRequest request) {
        log.info("EMAIL GOTTEN"+request.email());
        UserCredential userCredential = userCredentialRepo.findByEmail(request.email())
                .orElseThrow(() ->new RuntimeException("Data not Found"));
        userCredential.setKycVerified(true);
        userCredentialRepo.save(userCredential);


    }
}
