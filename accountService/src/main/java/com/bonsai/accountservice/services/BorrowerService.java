package com.bonsai.accountservice.services;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-06-17
 */
public interface BorrowerService {
    boolean isBorrowerEligibleForLoan(String email);
}
