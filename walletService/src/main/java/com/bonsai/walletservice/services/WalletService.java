package com.bonsai.walletservice.services;

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
    BigDecimal fetchBalanceFromWallet(String email);
    List<Map<String, Object>> findAllTransactionsByUserEmail(String email);
}
