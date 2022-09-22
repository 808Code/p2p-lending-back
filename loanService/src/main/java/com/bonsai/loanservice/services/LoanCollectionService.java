package com.bonsai.loanservice.services;

import java.util.UUID;

public interface LoanCollectionService {
    Long getLoanCollectionAmount(UUID loanId);
    UUID save(UUID loanId, String lenderEmail, UUID transactionId);

    UUID fulfillLoan(UUID loanId);
    void deleteAllByLoanRequestId(UUID loanId);
}
