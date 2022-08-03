package com.bonsai.sharedservice.dtos.loan;

import java.util.UUID;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-08-02
 */
public record LoanInQueue(
        UUID id,
        String borrower
) {}
