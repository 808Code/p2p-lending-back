package com.bonsai.accountservice.services;

import com.bonsai.accountservice.dto.request.GetKYCRequest;
import com.bonsai.accountservice.dto.response.UnverifiedKycResponse;
import com.bonsai.accountservice.models.KYC;

import java.util.List;
import java.util.UUID;

public interface KYCService {
    KYC getKYC(String request);
    void verifyKYC(String email);

    List<UnverifiedKycResponse> getAllUnverifiedKYC();

    void saveAdminKycMessage(String s,String email);

    String getAdminKycMessage(String email);
}
