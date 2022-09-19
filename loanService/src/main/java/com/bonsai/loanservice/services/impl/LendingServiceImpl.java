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
import com.bonsai.walletservice.constants.WalletTransactionTypes;
import com.bonsai.walletservice.dtos.WithdrawRequest;
import com.bonsai.walletservice.models.Wallet;
import com.bonsai.walletservice.models.WalletTransaction;
import com.bonsai.walletservice.repositories.WalletRepo;
import com.bonsai.walletservice.repositories.WalletTransactionRepo;
import com.bonsai.walletservice.services.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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

        Long amount = walletService.withdraw(new WithdrawRequest(lendRequest.amount(), "Lend amount to " + loanRequest.getBorrower().getEmail()), lenderEmail);

        loanCollection.setCollectedAmount(loanCollection.getCollectedAmount() + amount);
        loanCollection = collectionRepo.saveAndFlush(loanCollection);

        loanRequest.setRemainingAmount(loanRequest.getRemainingAmount() - amount);

        if (loanCollection.getCollectedAmount() == loanRequest.getAmount()) {
            loanRequest.setLoanStatus(LoanStatus.FULFILLED);
        }

        loanRequest = loanRepo.saveAndFlush(loanRequest);

        Wallet lenderWallet = walletRepo.findByUserEmail(loanRequest.getBorrower().getEmail());
        WalletTransaction walletTransaction = new WalletTransaction();
        walletTransaction.setWallet(lenderWallet);
        walletTransaction.setAmount(BigDecimal.valueOf(amount));
        walletTransaction.setDate(LocalDateTime.now());

        if (loanRequest.getLoanStatus().equals(LoanStatus.FULFILLED)) {
            //release that amount from lenders wallet
            walletService.loadWallet(loanCollection.getCollectedAmount(), loanRequest.getBorrower().getEmail());
            walletTransaction.setType(WalletTransactionTypes.RELEASED);
            walletTransaction.setRemarks("Amount Released for lending to " + loanRequest.getBorrower().getEmail());

        } else {
            //lock that amount in lenders wallet
            walletTransaction.setType(WalletTransactionTypes.LOCKED);
            walletTransaction.setRemarks("Amount Locked for lending to " + loanRequest.getBorrower().getEmail());
        }
        return amount;
    }

    //borrower ko account maa paisa gayena
    //5000 ko multiple maa matra hunu paryo lending amount
}
