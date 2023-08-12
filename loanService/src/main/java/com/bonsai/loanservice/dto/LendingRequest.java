package com.bonsai.loanservice.dto;

import javax.validation.constraints.NotNull;

public record LendingRequest(
        @NotNull(message = "Loan amount can't be empty")
        Long amount,

        @NotNull(message = "Lending duration can't be empty")
        Integer lendingDuration
) {
}
