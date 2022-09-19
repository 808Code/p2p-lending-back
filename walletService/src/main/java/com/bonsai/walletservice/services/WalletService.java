package com.bonsai.walletservice.services;

import com.bonsai.walletservice.dtos.WithdrawRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-08-06
 */
public interface WalletService {
    BigDecimal loadWallet(Long amount, String user);
    Map<String, BigDecimal> fetchBalanceFromWallet(String email);
    List<Map<String, Object>> findAllTransactionsByUserEmail(String email);
    Long withdraw(WithdrawRequest request, String user);
}
