package com.bonsai.loansuggestionservice.wallet.services;

import com.bonsai.loansuggestionservice.wallet.dtos.WalletLoadRequest;

import javax.transaction.Transactional;
import java.util.UUID;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-08-06
 */
public interface WalletTransactionService {

    @Transactional
    UUID createTransaction(WalletLoadRequest walletLoadRequest, String user);
}
