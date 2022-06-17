package com.bonsai.loanservice.dto;

import com.bonsai.loanservice.models.LoanRequest;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-06-17
 */
public record LoanResponse (
        UUID id,

        @NotNull(message = "Borrower id can't be null")
        UUID borrowerId,

        @NotNull(message = "Loan amount can't be null")
        @Min(value = 1000, message = "Loan amount can't be less than 1000")
        Long amount,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        @FutureOrPresent(message = "Past date not supported")
        LocalDate requestedDate,

        @NotNull(message = "Loan duration can't be null")
        @Min(value = 1, message = "Loan duration must be at-least 1 month")
        Integer duration,

        @NotEmpty(message = "Loan type can't be empty")
        String loanType,

        Boolean approvalStatus
) {
    public LoanResponse(LoanRequest loanRequest) {
        this(loanRequest.getId(), loanRequest.getBorrower().getId(), loanRequest.getAmount(), loanRequest.getRequestedDate(),
                loanRequest.getDuration(), loanRequest.getLoanType(), loanRequest.getApprovalStatus());
    }

    public static List<LoanResponse> loanToDtoList(List<LoanRequest> loanList) {
        return loanList.stream().map(
                loanRequest -> new LoanResponse(loanRequest)
        ).collect(Collectors.toList());
    }
}
