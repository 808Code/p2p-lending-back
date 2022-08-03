package com.bonsai.loansuggestionservice.services.impl;

import com.bonsai.accountservice.models.UserCredential;
import com.bonsai.accountservice.repositories.UserCredentialRepo;
import com.bonsai.loanservice.constants.LoanSuggestionStatus;
import com.bonsai.loanservice.dto.LoanResponse;
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
    private final UserCredentialRepo userCredentialRepo;

    @Override
    @Transactional
    public void save(UUID loanRequestId) {

        LoanRequest loanRequest = loanRequestRepo.findById(loanRequestId)
                .orElseThrow(() -> new AppException("Invalid loan id from queue", HttpStatus.INTERNAL_SERVER_ERROR));

        //finds relevant lenders on the basis of their last activity
        List<UserCredential> lenders = userCredentialRepo.findAllActiveLenders();

        //if no relevant lender is found change the loan suggestion status to terminated and throw an exception
        if (lenders.isEmpty()) {
            //if loan request can't be suggested change its status
            loanRequest.setSuggestionStatus(LoanSuggestionStatus.TERMINATED);
            loanRequestRepo.save(loanRequest);
            //haha i_am_teapot status cool laagyo ani throw gardiye
            throw new AppException("Loan request of id " + loanRequestId + " can't be suggested to any lenders",
                    HttpStatus.I_AM_A_TEAPOT);
        }

        //save the loan request into loan suggestion table
        //by linking it with all the lenders one by one
        for (UserCredential lender : lenders) {
            LoanSuggestion loanSuggestion = new LoanSuggestion();
            loanSuggestion.setLoanRequest(loanRequest);
            loanSuggestion.setLender(lender);
            //try block is used to rollback previous savings into database if any error occurs
            try {
                loanSuggestionRepo.save(loanSuggestion);
            } catch (Exception exception) {
                //if loan request can't be suggested change its status
                loanRequest.setSuggestionStatus(LoanSuggestionStatus.TERMINATED);
                loanRequestRepo.save(loanRequest);
                throw new AppException("Loan Suggestion can't be created", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        //once the loan is suggested to lenders change its suggestion status to suggested and save it into database
        loanRequest.setSuggestionStatus(LoanSuggestionStatus.SUGGESTED);
        loanRequestRepo.save(loanRequest);
    }

    @Override
    public List<LoanResponse> findSuggestedLoansForLender(String email) {
        return loanSuggestionRepo.findAllSuggestedLoansForLender(email);
    }
}
