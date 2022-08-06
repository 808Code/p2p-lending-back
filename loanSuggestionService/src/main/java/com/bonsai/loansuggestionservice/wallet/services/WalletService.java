package com.bonsai.loansuggestionservice.wallet.services;

import java.util.UUID;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-08-06
 */
public interface WalletService {
    public UUID loadWallet(Long amount, String user);
    Long fetchBalanceFromWallet(String email);
}
