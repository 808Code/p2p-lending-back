package com.bonsai.loansuggestionservice.wallet.services.impl;

import com.bonsai.loansuggestionservice.wallet.repositories.WalletRepo;
import com.bonsai.loansuggestionservice.wallet.services.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-08-06
 */
@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepo walletRepo;

    @Override
    public Long fetchBalanceFromWallet(String email) {
        return walletRepo.fetchBalanceFromWallet(email);
    }
}
