package com.bonsai.repaymentservice.dto;

import javax.validation.constraints.NotBlank;

public record PayInstallmentRequest(
        @NotBlank(message = "Installment Id cant be blank.")
        String installmentId,
        @NotBlank(message = "Loan Id cant be blank.")
        String loanId,
        @NotBlank(message = "email cant be blank.")
        String email

) {}
