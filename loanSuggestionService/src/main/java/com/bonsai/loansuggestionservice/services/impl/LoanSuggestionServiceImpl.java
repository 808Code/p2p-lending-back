package com.bonsai.loansuggestionservice.services.impl;

import com.bonsai.accountservice.constants.Roles;
import com.bonsai.accountservice.models.UserCredential;
import com.bonsai.accountservice.repositories.UserCredentialRepo;
import com.bonsai.loanservice.constants.LoanStatus;
import com.bonsai.loanservice.models.LoanRequest;
import com.bonsai.loanservice.repositories.LoanRequestRepo;
import com.bonsai.loansuggestionservice.models.LoanSuggestion;
import com.bonsai.loansuggestionservice.repositories.LoanSuggestionRepo;
import com.bonsai.loansuggestionservice.services.LoanSuggestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-08-03
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LoanSuggestionServiceImpl implements LoanSuggestionService {

    private final LoanSuggestionRepo loanSuggestionRepo;
    private final LoanRequestRepo loanRequestRepo;
    private final UserCredentialRepo userCredentialRepo;

    @Value("#{new Integer('${min.lender}')}")
    private Long MIN_LENDING_AMOUNT;

    @Value("#{new Integer('${max.normal.lender}')}")
    private Long MAX_LENDING_AMOUNT_NORMAL;

    @Value("#{new Integer('${max.institutional.lender}')}")
    private Long MAX_LENDING_AMOUNT_INSTITUTIONAL;

    @Override
    @Transactional
    public void save(UUID loanRequestId, String borrowerEmail) {

        LoanRequest loanRequest = loanRequestRepo.findById(loanRequestId).orElse(null);

        if (loanRequest == null) {
            log.error("Loan from queue doesn't exist");
            return;
        }

        UserCredential borrower = userCredentialRepo.findByEmailAndRole(borrowerEmail, Roles.BORROWER).orElse(null);

        if (borrower == null) {
            log.error("Borrower from queue doesn't exist");
            return;
        }

        //finds relevant lenders on the basis of their last activity
        List<UserCredential> lenders = userCredentialRepo.findAllActiveLenders();

        //if no relevant lender is found change the loan suggestion status to terminated and throw an exception
        if (lenders.isEmpty()) {
            //if loan request can't be suggested change its status
            loanRequest.setLoanStatus(LoanStatus.TERMINATED);
            borrower.setOngoingLoan(false);
            loanRequestRepo.save(loanRequest);
            userCredentialRepo.save(borrower);
            log.error("Sorry, no lenders found. Loan request has been terminated");
            return;
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
                loanRequest.setLoanStatus(LoanStatus.TERMINATED);
                borrower.setOngoingLoan(false);
                loanRequestRepo.save(loanRequest);
                userCredentialRepo.save(borrower);
                log.error("Loan Suggestion can't be created so loan has been terminated");
            }

        }

        //once the loan is suggested to lenders change its suggestion status to suggested and save it into database
        loanRequest.setLoanStatus(LoanStatus.SUGGESTED);
        loanRequestRepo.save(loanRequest);
    }

    @Override
    public List<Map<String, Object>> findSuggestedLoansForLender(String email) {

        //check lender type i.e. institutional or normal send max lending range accordingly
        //let's assume lender is normal as of now
        return loanSuggestionRepo.findAllSuggestedLoansForLender(email, MIN_LENDING_AMOUNT, MAX_LENDING_AMOUNT_NORMAL);
    }
}
