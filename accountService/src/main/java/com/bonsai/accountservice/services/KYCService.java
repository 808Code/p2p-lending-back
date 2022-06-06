package com.bonsai.accountservice.services;

import com.bonsai.accountservice.dto.request.GetKYCRequest;
import com.bonsai.accountservice.dto.request.VerifyKYCRequest;

public interface KYCService {
    void getKYC(GetKYCRequest request);
    void verifyKYC(VerifyKYCRequest request);
}
