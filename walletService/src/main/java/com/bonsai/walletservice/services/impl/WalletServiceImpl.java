package com.bonsai.walletservice.services.impl;

import com.bonsai.accountservice.models.UserCredential;
import com.bonsai.accountservice.repositories.UserCredentialRepo;
import com.bonsai.walletservice.constants.WalletTransactionTypes;
import com.bonsai.walletservice.dtos.LoadWalletDto;
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
import java.util.UUID;

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

    @Override
    public Wallet findUserWallet(String email) {
        Wallet wallet = walletRepo.findByUserEmail(email);
        if (wallet == null) {
            throw new AppException("Wallet not found", HttpStatus.BAD_REQUEST);
        }
        return wallet;
    }

    @Transactional
    @Override
    public LoadWalletDto loadWallet(BigDecimal amount, String user, String remarks) {

        //get the wallet of the given user
        Wallet wallet = findUserWallet(user);

        //add amount to wallet
        wallet.setAmount(wallet.getAmount().add(amount));

        //build transaction for this operation
        WalletTransaction walletTransaction = new WalletTransaction();
        walletTransaction.setWallet(wallet);
        walletTransaction.setDate(LocalDateTime.now());
        walletTransaction.setAmount(amount);
        walletTransaction.setType(WalletTransactionTypes.CREDIT);
        walletTransaction.setRemarks(remarks);

        //save updated wallet into database
        walletRepo.save(wallet);
        //create new transaction and save it into database
        walletTransactionRepo.save(walletTransaction);

        return new LoadWalletDto(amount,walletTransaction);
    }

    @Override
    public Map<String, BigDecimal> fetchBalanceFromWallet(String email) {
        UserCredential userCredential = userCredentialRepo.findByEmail(email)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.BAD_REQUEST));
        return walletRepo.fetchBalanceFromWallet(userCredential.getEmail());
    }

    @Override
    public List<Map<String, Object>> findAllTransactionsByUserEmail(String email) {
        UserCredential userCredential = userCredentialRepo.findByEmail(email)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.BAD_REQUEST));
        return walletTransactionRepo.findAllTransactionsByUserEmail(userCredential.getEmail());
    }

    @Override
    public UUID debitOrLockAmount(String transactionType, BigDecimal amount, String userEmail) {

        transactionType = transactionType.toUpperCase();

        //if transaction type is neither DEBIT nor LOCKED, throw an exception
        if (!transactionType.equals(WalletTransactionTypes.DEBIT)
                && !transactionType.equals(WalletTransactionTypes.LOCKED)) {
            throw new AppException("Transaction type invalid", HttpStatus.BAD_REQUEST);
        }

        //get the wallet of the given user
        Wallet wallet = findUserWallet(userEmail);

        //check whether user's balance is sufficient or not
        if (!isBalanceSufficient(userEmail, amount)) {
            throw new AppException("Sorry, your balance is insufficient", HttpStatus.BAD_REQUEST);
        }

        //build transaction for this operation
        WalletTransaction walletTransaction = new WalletTransaction();
        walletTransaction.setWallet(wallet);
        walletTransaction.setDate(LocalDateTime.now());
        walletTransaction.setAmount(amount);
        walletTransaction.setType(transactionType);
        walletTransaction.setRemarks(
                transactionType.equals(WalletTransactionTypes.DEBIT)
                        ? "Rs. " + amount + " debited from wallet."
                        : "Rs. " + amount + " locked in wallet."
        );

        //subtract amount from wallet if transaction type is DEBIT
        if (transactionType.equals(WalletTransactionTypes.DEBIT)) {
            wallet.setAmount(wallet.getAmount().subtract(amount));
            walletRepo.saveAndFlush(wallet);
        }

        //create new transaction and save it into database
        walletTransaction = walletTransactionRepo.saveAndFlush(walletTransaction);

        return walletTransaction.getId();
    }

    @Override
    public Boolean isBalanceSufficient(String userEmail, BigDecimal amount) {
        BigDecimal availableBalance = fetchBalanceFromWallet(userEmail).get("availableBalance");
        return (availableBalance.compareTo(amount) > 0) || (availableBalance.equals(amount));
    }

}
