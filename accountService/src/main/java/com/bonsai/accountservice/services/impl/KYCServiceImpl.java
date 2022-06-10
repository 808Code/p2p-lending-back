package com.bonsai.accountservice.services.impl;

import com.bonsai.accountservice.dto.request.GetKYCRequest;
import com.bonsai.accountservice.dto.request.VerifyKYCRequest;
import com.bonsai.accountservice.models.KYC;
import com.bonsai.accountservice.models.UserCredential;
import com.bonsai.accountservice.repositories.KYCRepo;
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
    private final KYCRepo kycRepo;

    @Override
    public KYC getKYC(GetKYCRequest request) {
        UserCredential userCredential = userCredentialRepo.findByEmail(request.email())
                .orElseThrow(() ->new AppException("Email not found in database.", HttpStatus.NOT_FOUND));
        KYC kyc = kycRepo.findById(userCredential.getKyc().getId())
                .orElseThrow(() ->new AppException("KYC not found in database.", HttpStatus.NOT_FOUND));

        return kyc;
    }

    @Override
    public void verifyKYC(VerifyKYCRequest request) {
        UserCredential userCredential = userCredentialRepo.findByEmail(request.email())
                .orElseThrow(() ->new AppException("Email not found in database.",HttpStatus.NOT_FOUND));
        userCredential.setKycVerified(true);
        userCredentialRepo.save(userCredential);

    }
}
