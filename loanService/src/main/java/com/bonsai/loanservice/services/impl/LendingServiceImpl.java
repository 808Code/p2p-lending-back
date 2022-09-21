package com.bonsai.loanservice.services.impl;

import com.bonsai.accountservice.constants.Roles;
import com.bonsai.accountservice.models.UserCredential;
import com.bonsai.accountservice.repositories.UserCredentialRepo;
import com.bonsai.loanservice.constants.LoanStatus;
import com.bonsai.loanservice.dto.LendRequest;
import com.bonsai.loanservice.models.LoanCollection;
import com.bonsai.loanservice.models.LoanRequest;
import com.bonsai.loanservice.repositories.LoanCollectionRepo;
import com.bonsai.loanservice.repositories.LoanRequestRepo;
import com.bonsai.loanservice.services.LendingService;
import com.bonsai.sharedservice.exceptions.AppException;
import com.bonsai.walletservice.repositories.WalletRepo;
import com.bonsai.walletservice.repositories.WalletTransactionRepo;
import com.bonsai.walletservice.services.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LendingServiceImpl implements LendingService {

    private final LoanCollectionRepo collectionRepo;
    private final LoanRequestRepo loanRepo;
    private final UserCredentialRepo userRepo;
    private final WalletService walletService;
    private final WalletTransactionRepo transactionRepo;
    private final WalletRepo walletRepo;

    @Override
    @Transactional
    public Long lend(LendRequest lendRequest, String lenderEmail) {

        if (lendRequest.amount() % 5000 != 0) {
            throw new AppException("Lending amount must be in the multiple of 5", HttpStatus.BAD_REQUEST);
        }

        LoanRequest loanRequest = loanRepo.findById(UUID.fromString(lendRequest.loanId()))
                .orElseThrow(() -> new AppException("Invalid loan Id", HttpStatus.BAD_REQUEST));

        if (loanRequest.getLoanStatus().equals(LoanStatus.FULFILLED)) {
            throw new AppException("Loan has already been fulfilled", HttpStatus.BAD_REQUEST);
        }

        if (loanRequest.getLoanStatus().equals(LoanStatus.REJECTED)) {
            throw new AppException("Loan has been rejected", HttpStatus.BAD_REQUEST);
        }

        UserCredential lender = userRepo.findByEmailAndRole(lenderEmail, Roles.LENDER)
                .orElseThrow(() -> new AppException("Invalid lender Id", HttpStatus.BAD_REQUEST));

        LoanCollection loanCollection = collectionRepo.findByLoanRequest_Id(loanRequest.getId())
                .orElseThrow(() -> new AppException("No loan collection found", HttpStatus.INTERNAL_SERVER_ERROR));

        Long lenderWalletBalance = walletService.fetchBalanceFromWallet(lenderEmail).get("availableBalance").longValue();

        Long loanAmount = lendRequest.amount();

        //does lender have enough amount to pay for the loan?
        if (lenderWalletBalance < loanAmount) {
            throw new AppException("Sorry, you have insufficient balance", HttpStatus.BAD_REQUEST);
        }

        //will loan be fulfilled?


        //no = lender ko wallet baata amount locked state maa rakhne
        //add amount to loan collection table (loan collecteion has lender and walletTransaction column)

        //yes = lender ko wallet lai debit(amount) garne
        //loan collection maa vaako lender ko chai transaction lai locked baata debit maa edit garne

        //credit to borrower wallet
        //update my lendings on the basis

        //clear loan collection


        return null;
    }
}
