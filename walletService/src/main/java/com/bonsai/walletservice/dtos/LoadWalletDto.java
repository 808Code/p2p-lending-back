package com.bonsai.walletservice.dtos;

import com.bonsai.walletservice.models.WalletTransaction;

import java.math.BigDecimal;

public record LoadWalletDto(
        BigDecimal amount,
        WalletTransaction walletTransaction
) {
}
