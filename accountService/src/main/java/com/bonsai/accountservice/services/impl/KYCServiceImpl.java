package com.bonsai.accountservice.services.impl;

import com.bonsai.accountservice.dto.request.GetKYCRequest;
import com.bonsai.accountservice.models.KYC;
import com.bonsai.accountservice.models.UserCredential;
import com.bonsai.accountservice.repositories.UserCredentialRepo;
import com.bonsai.accountservice.services.KYCService;
import com.bonsai.sharedservice.exceptions.AppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;



@Service
@Slf4j
@RequiredArgsConstructor
public class KYCServiceImpl implements KYCService {
    private final UserCredentialRepo userCredentialRepo;

    @Override
    public KYC getKYC(GetKYCRequest request) {
        UserCredential userCredential = userCredentialRepo.findByEmail(request.email())
                .orElseThrow(() ->new AppException("Email not found in database.", HttpStatus.NOT_FOUND));

        KYC kyc = userCredential.getKyc();
        kyc.setVerified(userCredential.isKycVerified());

        return kyc;
    }

    @Override
    public void verifyKYC(String email) {
        UserCredential userCredential = userCredentialRepo.findByEmail(email)
                .orElseThrow(() ->new AppException("Email not found in database.",HttpStatus.NOT_FOUND));
        userCredential.setKycVerified(true);
        userCredentialRepo.save(userCredential);

    }
}
