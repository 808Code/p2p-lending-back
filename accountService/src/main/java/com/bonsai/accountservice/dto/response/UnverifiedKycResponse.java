package com.bonsai.accountservice.dto.response;

public record UnverifiedKycResponse(
        String KYCId,

        String fullName,
        String lastUpdated,
        String UserType,
        String email

) {
}
