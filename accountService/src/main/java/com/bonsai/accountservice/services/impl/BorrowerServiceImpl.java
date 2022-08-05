package com.bonsai.accountservice.services.impl;

import com.bonsai.accountservice.constants.Roles;
import com.bonsai.accountservice.dto.borrower.BorrowerEligibilityResponse;
import com.bonsai.accountservice.models.UserCredential;
import com.bonsai.accountservice.repositories.UserCredentialRepo;
import com.bonsai.accountservice.services.BorrowerService;
import com.bonsai.sharedservice.exceptions.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-06-17
 */
@Service
@RequiredArgsConstructor
public class BorrowerServiceImpl implements BorrowerService {

    private final UserCredentialRepo userCredentialRepo;

    @Override
    public BorrowerEligibilityResponse isBorrowerEligibleForLoan(String email) {
        UserCredential borrower = userCredentialRepo.findByEmailAndRole(email, Roles.BORROWER).orElseThrow(
                () -> new AppException("Borrower not found", HttpStatus.BAD_REQUEST)
        );

        if (!borrower.isKycVerified()) {
            return new BorrowerEligibilityResponse(false, "Your kyc is not verified yet");
        }
        if (borrower.isOngoingLoan()) {
            return new BorrowerEligibilityResponse(false, "You have an ongoing loan");
        }
        return new BorrowerEligibilityResponse(true, null);
    }
}
