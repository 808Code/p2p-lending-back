package com.bonsai.walletservice.services;

import com.bonsai.walletservice.constants.WalletTransactionTypes;
import com.bonsai.walletservice.models.Wallet;

import java.math.BigDecimal;
import java.math.BigInteger;
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
    BigDecimal loadWallet(Long amount, String user);
    Map<String, BigDecimal> fetchBalanceFromWallet(String email);
    List<Map<String, Object>> findAllTransactionsByUserEmail(String email);
    UUID debitOrLockAmount(String transactionType, Long amount, String userEmail);
    Boolean isBalanceSufficient(String user, BigDecimal amount);
}
