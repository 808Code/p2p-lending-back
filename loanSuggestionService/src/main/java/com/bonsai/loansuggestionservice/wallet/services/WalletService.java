package com.bonsai.loansuggestionservice.wallet.services;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-08-06
 */
public interface WalletService {
    BigDecimal loadWallet(Long amount, String user);
    BigDecimal fetchBalanceFromWallet(String email);
}
