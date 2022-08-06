package com.bonsai.loansuggestionservice.wallet.services;

import javax.transaction.Transactional;
import java.util.UUID;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-08-06
 */
public interface WalletTransactionService {

    @Transactional
    UUID loadWallet(Long amount, String user);
}
