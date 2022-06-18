package com.bonsai.loanservice.dto;

import com.bonsai.loanservice.models.LoanRequest;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-05-25
 */
public record LoanRequestDto(
        @NotEmpty(message = "Borrower email can't be empty")
        @Email(message = "Borrower email is invalid")
        String borrower,

        @NotNull(message = "Loan amount can't be null")
        @Min(value = 1000, message = "Loan amount can't be less than 1000")
        Long amount,

        @NotNull(message = "Loan duration can't be null")
        @Min(value = 1, message = "Loan duration must be at-least 1 month")
        Integer duration,

        @NotEmpty(message = "Loan type can't be empty")
        String loanType
) {

    public LoanRequestDto(LoanRequest loanRequest) {
        this(loanRequest.getBorrower().getEmail(), loanRequest.getAmount(),
                loanRequest.getDuration(), loanRequest.getLoanType());
    }

    public static List<LoanRequestDto> loanToDtoList(List<LoanRequest> loanList) {
        return loanList.stream().map(
                loanRequest -> new LoanRequestDto(loanRequest)
        ).collect(Collectors.toList());
    }

}
