package com.bonsai.walletservice.services.impl;

import com.bonsai.accountservice.models.UserCredential;
import com.bonsai.accountservice.repositories.UserCredentialRepo;
import com.bonsai.walletservice.constants.WalletTransactionTypes;
import com.bonsai.walletservice.models.Wallet;
import com.bonsai.walletservice.models.WalletTransaction;
import com.bonsai.walletservice.repositories.WalletRepo;
import com.bonsai.walletservice.repositories.WalletTransactionRepo;
import com.bonsai.walletservice.services.WalletService;
import com.bonsai.sharedservice.exceptions.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-08-06
 */
@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepo walletRepo;
    private final UserCredentialRepo userCredentialRepo;
    private final WalletTransactionRepo walletTransactionRepo;

    @Transactional
    @Override
    public BigDecimal loadWallet(Long amount, String user) {

        //get the wallet of the given user
        Wallet wallet = walletRepo.findByUserEmail(user);

        if(wallet == null) {
            throw new AppException("User of given email not found", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //add amount to wallet
        wallet.setAmount(wallet.getAmount().add(BigDecimal.valueOf(amount)));

        //build transaction for this operation
        WalletTransaction walletTransaction = new WalletTransaction();
        walletTransaction.setWallet(wallet);
        walletTransaction.setDate(LocalDateTime.now());
        walletTransaction.setAmount(BigDecimal.valueOf(amount));
        walletTransaction.setType(WalletTransactionTypes.CREDIT);
        walletTransaction.setRemarks("Amount Loaded into wallet");

        //save updated wallet into database
        walletRepo.save(wallet);
        //create new transaction and save it into database
        walletTransactionRepo.save(walletTransaction);

        return wallet.getAmount();
    }
    @Override
    public BigDecimal fetchBalanceFromWallet(String email) {
        BigDecimal walletBalance = walletRepo.fetchBalanceFromWallet(email);

        return walletBalance == null ? BigDecimal.ZERO : walletBalance;
    }

    @Override
    public List<Map<String, Object>> findAllTransactionsByUserEmail(String email) {
        return walletTransactionRepo.findAllTransactionsByUserEmail(email);
    }
}