package com.bonsai.accountservice.dto.borrower;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-06-18
 */
public record BorrowerEligibilityResponse(
        boolean eligible,
        String reasonForIneligibility
) {}
