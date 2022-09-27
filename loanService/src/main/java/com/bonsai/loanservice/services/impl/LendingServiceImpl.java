package com.bonsai.loanservice.services.impl;

import com.bonsai.accountservice.constants.Roles;
import com.bonsai.accountservice.models.UserCredential;
import com.bonsai.accountservice.repositories.UserCredentialRepo;
import com.bonsai.loanservice.constants.LoanStatus;
import com.bonsai.loanservice.dto.LendRequest;
import com.bonsai.loanservice.dto.LendingResponse;
import com.bonsai.loanservice.models.Lending;
import com.bonsai.loanservice.models.LoanCollection;
import com.bonsai.loanservice.models.LoanRequest;
import com.bonsai.loanservice.repositories.LendingRepo;
import com.bonsai.loanservice.repositories.LoanCollectionRepo;
import com.bonsai.loanservice.repositories.LoanRequestRepo;
import com.bonsai.loanservice.services.LendingService;
import com.bonsai.loanservice.services.LoanCollectionService;
import com.bonsai.loanservice.services.LoanService;
import com.bonsai.sharedservice.exceptions.AppException;
import com.bonsai.walletservice.constants.WalletTransactionTypes;
import com.bonsai.walletservice.models.WalletTransaction;
import com.bonsai.walletservice.repositories.WalletTransactionRepo;
import com.bonsai.walletservice.services.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LendingServiceImpl implements LendingService {

    private final LoanRequestRepo loanRepo;
    private final UserCredentialRepo userRepo;
    private final WalletService walletService;
    private final WalletTransactionRepo transactionRepo;
    private final LoanCollectionService loanCollectionService;
    private final LendingRepo lendingRepo;
    private final LoanCollectionRepo loanCollectionRepo;
    private final LoanService loanService;

    @Override
    @Transactional
    public Long lend(LendRequest lendRequest, String lenderEmail) {

        //A lender can lend amount only in the multiple of 5000
        if (lendRequest.amount() % 5000 != 0) {
            throw new AppException("Lending amount must be in the multiple of 5000", HttpStatus.BAD_REQUEST);
        }

        //fetch loan from database
        LoanRequest loanRequest = loanRepo.findById(UUID.fromString(lendRequest.loanId()))
                .orElseThrow(() -> new AppException("Invalid loan Id", HttpStatus.BAD_REQUEST));

        //lenders can't lend amount to already fulfilled loan
        if (loanRequest.getLoanStatus().equals(LoanStatus.ONGOING)) {
            throw new AppException("Loan has already been fulfilled", HttpStatus.BAD_REQUEST);
        }

        //loan is rejected if it wasn't suggested to any lenders
        if (loanRequest.getLoanStatus().equals(LoanStatus.REJECTED)) {
            throw new AppException("Loan has been rejected", HttpStatus.BAD_REQUEST);
        }

        //to check whether lender is valid or not
        UserCredential lender = userRepo.findByEmailAndRole(lenderEmail, Roles.LENDER)
                .orElseThrow(() -> new AppException("Invalid lender Id", HttpStatus.BAD_REQUEST));


        Long lendingAmount = lendRequest.amount();

        //does lender have enough amount to pay for the loan?
        if (!walletService.isBalanceSufficient(lenderEmail, BigDecimal.valueOf(lendingAmount))) {
            throw new AppException("Sorry, you have insufficient balance", HttpStatus.BAD_REQUEST);
        }

        //will loan be fulfilled?
        Long collectionAmount = loanCollectionService.getLoanCollectionAmount(loanRequest.getId());
        Long futureCollectionAmount = collectionAmount + lendingAmount;

        //if 'NO' put lendingAmount in LOCKED state in lender's wallet
        //add amount to loan collection table by keeping the locked transaction
        if (!futureCollectionAmount.equals(loanRequest.getAmount())) {

            UUID transactionId = walletService.debitOrLockAmount(WalletTransactionTypes.LOCKED, lendingAmount, lenderEmail);
            loanCollectionService.save(loanRequest.getId(), lenderEmail, transactionId);

            loanRequest.setRemainingAmount(loanRequest.getRemainingAmount() - lendingAmount);
            loanRepo.save(loanRequest);

            //clear loan suggestion
            loanService.deleteLoanSuggestion(loanRequest.getId(), lenderEmail);

            return lendRequest.amount();
        }

        //yes = lender ko wallet lai debit(amount) garne
        UUID transactionId = walletService.debitOrLockAmount(WalletTransactionTypes.DEBIT, lendingAmount, lenderEmail);
        loanCollectionService.save(loanRequest.getId(), lenderEmail, transactionId);
        //create lending whenever there occurs a "DEBIT" instantly
        createLending(LocalDateTime.now(), lenderEmail, loanRequest.getId(), transactionId);

        //clear loan suggestion
        loanService.deleteLoanSuggestion(loanRequest.getId(), lenderEmail);

        //change the type of transaction from "LOCKED" to "DEBIT" in all the loanCollections
        //also deduct that "DEBIT" amount from lender's wallet
        //update lending table accordingly for all the lenders
        loanCollectionService.fulfillLoan(loanRequest.getId());

        //credit to borrower wallet after loan is fulfilled
        Long newCollectedAmount = loanCollectionService.getLoanCollectionAmount(loanRequest.getId());
        walletService.loadWallet(newCollectedAmount,
                loanRequest.getBorrower().getEmail(),
                "Loan amount credited into wallet");

        //clear loan collection after loan is fulfilled
        loanCollectionService.deleteAllByLoanRequestId(loanRequest.getId());

        return lendRequest.amount();
    }

    @Transactional
    @Override
    public UUID createLending(LocalDateTime lentDate, String lenderEmail, UUID loanRequestId, UUID transactionId) {

        UserCredential lender = userRepo.findByEmailAndRole(lenderEmail, Roles.LENDER)
                .orElseThrow(() -> new AppException("Lender not found", HttpStatus.BAD_REQUEST));

        LoanRequest loanRequest = loanRepo.findById(loanRequestId)
                .orElseThrow(() -> new AppException("Loan request not found", HttpStatus.BAD_REQUEST));

        WalletTransaction transaction = transactionRepo.findById(transactionId)
                .orElseThrow(() -> new AppException("Wallet transaction not found", HttpStatus.BAD_REQUEST));

        Lending lending = Lending.builder()
                .lentDate(lentDate)
                .lender(lender)
                .loanRequest(loanRequest)
                .transaction(transaction)
                .build();

        lending = lendingRepo.saveAndFlush(lending);
        return lending.getId();
    }

    @Override
    public List<LendingResponse> fetchLendings(String lenderEmail) {
        List<LendingResponse> response = new ArrayList<>();

        List<Lending> lendings = lendingRepo.findAllByLender_Email(lenderEmail);
        List<LoanCollection> loanCollectionList = loanCollectionRepo.findAllByLender_Email(lenderEmail);

        lendings.stream().forEach(lending -> response.add(new LendingResponse(lending)));
        loanCollectionList.stream().forEach(collection -> response.add(new LendingResponse(collection)));
        return response;
    }

}
