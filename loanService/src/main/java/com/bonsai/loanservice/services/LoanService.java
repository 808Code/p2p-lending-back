package com.bonsai.loanservice.services;

import com.bonsai.loanservice.dto.LoanRequestDto;
import com.bonsai.loanservice.dto.LoanResponse;

import java.util.List;
import java.util.UUID;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-05-25
 */
public interface LoanService {
    LoanResponse save(LoanRequestDto loanRequestDto);
    LoanRequestDto findById(UUID id);

    List<LoanResponse> findAllByBorrower(String borrowerEmail);

    List<String> findAllLoanTypes();
    void deleteLoanSuggestion(UUID loanId, String lenderEmail);
}
