package com.bonsai.loansuggestionservice.services.impl;

import com.bonsai.accountservice.models.UserCredential;
import com.bonsai.loanservice.models.LoanRequest;
import com.bonsai.loanservice.repositories.LoanRequestRepo;
import com.bonsai.loansuggestionservice.models.LoanSuggestion;
import com.bonsai.loansuggestionservice.repositories.LoanSuggestionRepo;
import com.bonsai.loansuggestionservice.services.LoanSuggestionService;
import com.bonsai.sharedservice.exceptions.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-08-03
 */
@Service
@RequiredArgsConstructor
public class LoanSuggestionServiceImpl implements LoanSuggestionService {

    private final LoanSuggestionRepo loanSuggestionRepo;
    private final LoanRequestRepo loanRequestRepo;

    @Override
    @Transactional
    public void save(UUID loanRequestId, List<UserCredential> lenders) {

        LoanRequest loanRequest = loanRequestRepo.findById(loanRequestId)
                .orElseThrow(() -> new AppException("Invalid loan id from queue", HttpStatus.INTERNAL_SERVER_ERROR));

        for (UserCredential lender : lenders) {
            try {
                LoanSuggestion loanSuggestion = new LoanSuggestion();
                loanSuggestion.setLoanRequest(loanRequest);
                loanSuggestion.setLender(lender);
                loanSuggestionRepo.save(loanSuggestion);
            } catch (Exception exception) {
                throw new AppException("Loan Suggestion can't be created", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }
}
