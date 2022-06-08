package com.bonsai.accountservice.services;

import com.bonsai.accountservice.dto.request.GetKYCRequest;
import com.bonsai.accountservice.dto.request.VerifyKYCRequest;
import com.bonsai.accountservice.models.KYC;

public interface KYCService {
    KYC getKYC(GetKYCRequest request);
    void verifyKYC(VerifyKYCRequest request);
}
