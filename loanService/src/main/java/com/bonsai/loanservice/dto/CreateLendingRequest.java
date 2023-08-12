package com.bonsai.loanservice.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public record CreateLendingRequest(
        @NotNull(message = "Loan amount can't be null")
        Long amount,

        @NotEmpty(message = "Loan Id can't be empty")
        String loanId
) {}
