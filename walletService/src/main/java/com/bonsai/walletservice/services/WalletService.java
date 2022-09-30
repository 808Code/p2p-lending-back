package com.bonsai.walletservice.services;

import com.bonsai.walletservice.dtos.LoadWalletDto;
import com.bonsai.walletservice.models.Wallet;
import com.bonsai.walletservice.models.WalletTransaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-08-06
 */
public interface WalletService {
    Wallet findUserWallet(String email);
    LoadWalletDto loadWallet(BigDecimal amount, String user, String remarks);
    Map<String, BigDecimal> fetchBalanceFromWallet(String email);
    List<Map<String, Object>> findAllTransactionsByUserEmail(String email);
    UUID debitOrLockAmount(String transactionType, BigDecimal amount, String userEmail);
    Boolean isBalanceSufficient(String user, BigDecimal amount);
}
