package com.bonsai.loansuggestionservice.wallet.services;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-08-06
 */
public interface WalletService {
    Long fetchBalanceFromWallet(String email);
}
