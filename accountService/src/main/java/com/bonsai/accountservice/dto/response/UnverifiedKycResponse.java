package com.bonsai.accountservice.dto.response;

import com.bonsai.accountservice.models.KYC;
import com.bonsai.accountservice.models.UserCredential;

import java.time.format.DateTimeFormatter;

public record UnverifiedKycResponse(
        String KYCId,

        String fullName,
        String lastUpdated,
        String userType,
        String email

) {
    public UnverifiedKycResponse(KYC kyc, UserCredential userCredential) {
        this(
                kyc.getId().toString(),
                kyc.getFirstName() + " " + (kyc.getMiddleName() != null ? kyc.getMiddleName() : "") + " " + kyc.getLastName(),
                kyc.getLastModifiedDate().format(DateTimeFormatter.ofPattern("YYYY-MM-dd")) + " At " + kyc.getLastModifiedDate().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                userCredential.getRole(),
                userCredential.getEmail()
        );
    }
}
