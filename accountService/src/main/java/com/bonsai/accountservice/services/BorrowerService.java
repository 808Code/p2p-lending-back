package com.bonsai.accountservice.services;

import com.bonsai.accountservice.dto.borrower.BorrowerEligibilityResponse;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-06-17
 */
public interface BorrowerService {
    BorrowerEligibilityResponse isBorrowerEligibleForLoan(String email);
}
