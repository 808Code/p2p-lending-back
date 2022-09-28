package com.bonsai.repaymentservice.dto;

import javax.validation.constraints.NotBlank;

public record GetInstallmentsDto (
        @NotBlank(message = "LoanId cant be Blank.")
        String loanId
){}
