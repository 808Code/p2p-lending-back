package com.bonsai.loansuggestionservice.wallet.services.impl;

import com.bonsai.accountservice.models.UserCredential;
import com.bonsai.accountservice.repositories.UserCredentialRepo;
import com.bonsai.loansuggestionservice.wallet.constants.WalletTransactionTypes;
import com.bonsai.loansuggestionservice.wallet.dtos.WalletLoadRequest;
import com.bonsai.loansuggestionservice.wallet.models.Wallet;
import com.bonsai.loansuggestionservice.wallet.models.WalletTransaction;
import com.bonsai.loansuggestionservice.wallet.repositories.WalletRepo;
import com.bonsai.loansuggestionservice.wallet.repositories.WalletTransactionRepo;
import com.bonsai.loansuggestionservice.wallet.services.WalletTransactionService;
import com.bonsai.sharedservice.exceptions.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.UUID;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-08-06
 */
@Service
@RequiredArgsConstructor
public class WalletTransactionServiceImpl implements WalletTransactionService {

    private final WalletRepo walletRepo;
    private final UserCredentialRepo userCredentialRepo;
    private final WalletTransactionRepo walletTransactionRepo;

    @Transactional
    @Override
    public UUID createTransaction(WalletLoadRequest walletLoadRequest, String user) {
        Wallet wallet = walletRepo.findByUserEmail(user);

        //if user doesn't have wallet create it
        if (wallet == null) {
            UserCredential userCredential = userCredentialRepo.findByEmail(user)
                    .orElseThrow(() -> new AppException("Logged in user is invalid", HttpStatus.INTERNAL_SERVER_ERROR));
            //create user wallet with zero balance
            //need to change amount datatype from long to double
            wallet = walletRepo.saveAndFlush(
                    Wallet.builder()
                            .amount(0L)
                            .user(userCredential)
                            .build()
            );
        }

        WalletTransaction walletTransaction = new WalletTransaction();
        walletTransaction.setWallet(wallet);
        walletTransaction.setDate(LocalDate.now());

        //in case of loading amount into wallet
        if (walletLoadRequest.transactionType().equalsIgnoreCase(WalletTransactionTypes.CREDIT)) {
            wallet.setAmount(wallet.getAmount() + walletLoadRequest.amount());
            walletTransaction.setType(WalletTransactionTypes.CREDIT);
        }

        //in case of amount withdrawal
        else if (walletLoadRequest.transactionType().equalsIgnoreCase(WalletTransactionTypes.DEBIT)) {

            //in case of insufficient balance abort
            if (wallet.getAmount() < walletLoadRequest.amount()) {
                throw new AppException("Insufficient balance in the wallet", HttpStatus.BAD_REQUEST);
            }

            wallet.setAmount(wallet.getAmount() - walletLoadRequest.amount());
            walletTransaction.setType(WalletTransactionTypes.DEBIT);
        }

        //there is also locked state, that functionality will be added in future
        else {
            throw new AppException("Invalid load type", HttpStatus.BAD_REQUEST);
        }

        walletTransaction.setRemarks(walletLoadRequest.remarks());
        walletRepo.save(wallet);
        walletTransactionRepo.save(walletTransaction);
        return wallet.getId();
    }
}
