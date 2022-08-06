package com.bonsai.loansuggestionservice.wallet.dtos;

import javax.validation.constraints.NotNull;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-08-06
 */
public record LoadWalletRequest (
        @NotNull
        Long amount
) {}
