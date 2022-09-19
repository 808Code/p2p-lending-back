package com.bonsai.walletservice.dtos;

import javax.validation.constraints.NotNull;

public record WithdrawRequest (
        @NotNull(message = "Amount to be withdrawn can't be null")
        Long amount,

        String remarks
) {}
