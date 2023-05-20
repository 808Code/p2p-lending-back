package com.bonsai.accountservice.services;

import com.bonsai.accountservice.dto.request.GetKYCRequest;
import com.bonsai.accountservice.models.KYC;

import java.util.List;

public interface KYCService {
    KYC getKYC(String request);
    void verifyKYC(String email);

    List<KYC> getAllUnverifiedKYC();
}
