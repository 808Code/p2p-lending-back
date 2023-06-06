package com.bonsai.accountservice.services.impl;

import com.bonsai.accountservice.dto.response.UnverifiedKycResponse;
import com.bonsai.accountservice.models.KYC;
import com.bonsai.accountservice.models.UserCredential;
import com.bonsai.accountservice.repositories.UserCredentialRepo;
import com.bonsai.accountservice.services.KYCService;
import com.bonsai.sharedservice.exceptions.AppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@Slf4j
@RequiredArgsConstructor
public class KYCServiceImpl implements KYCService {
    private final UserCredentialRepo userCredentialRepo;

    @Override
    public KYC getKYC(String request) {
        UserCredential userCredential = userCredentialRepo.findByEmail(request)
                .orElseThrow(() -> new AppException("Email not found in database.", HttpStatus.NOT_FOUND));

        if (userCredential.getKyc() == null)
            throw new AppException("KYC not found in database.", HttpStatus.NOT_FOUND);
        KYC kyc = userCredential.getKyc();

        kyc.setVerified(userCredential.isKycVerified());

        return kyc;
    }

    @Override
    public void verifyKYC(String email) {
        UserCredential userCredential = userCredentialRepo.findByEmail(email)
                .orElseThrow(() -> new AppException("Email not found in database.", HttpStatus.NOT_FOUND));
        userCredential.setKycVerified(true);
        userCredentialRepo.save(userCredential);

    }

    @Override
    public List<UnverifiedKycResponse> getAllUnverifiedKYC() {
        List<UserCredential> kycUnverifiedUsers = userCredentialRepo.findAllKycUnverifiedUsers();
        List<UnverifiedKycResponse> unverifiedKycResponseList = new ArrayList<>();
        for (UserCredential userCredential : kycUnverifiedUsers) {
            KYC kyc = userCredential.getKyc();
            if (kyc != null) {
                String fullName = kyc.getFirstName() + " " + (kyc.getMiddleName() != null ? kyc.getMiddleName() : "") + " " + kyc.getLastName();
                UnverifiedKycResponse unverifiedKycResponse = new UnverifiedKycResponse(kyc, userCredential);
                unverifiedKycResponseList.add(unverifiedKycResponse);
            }
        }
        return unverifiedKycResponseList;
    }

    @Transactional
    @Override
    public void saveAdminKycMessage(String message, UUID kycId) {
        userCredentialRepo.saveAdminKycMessage(message, kycId);
    }
}
