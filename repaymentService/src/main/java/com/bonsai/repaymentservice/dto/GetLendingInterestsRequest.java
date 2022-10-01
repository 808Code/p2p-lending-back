package com.bonsai.repaymentservice.dto;

import javax.validation.constraints.NotBlank;

public record GetLendingInterestsRequest(
        @NotBlank(message = "Installment Id cant be blank.")
        String lendingId
) {
}
