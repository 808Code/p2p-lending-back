package com.bonsai.loanservice.dto;

import com.bonsai.loanservice.models.Loan;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-05-25
 */
public record LoanDto(
        UUID id,

        @NotNull(message = "Borrower id can't be null")
        UUID borrowerId,

        @NotNull(message = "Loan amount can't be null")
        @Min(value = 1000, message = "Loan amount can't be less than 1000")
        Long amount,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        @FutureOrPresent(message = "Past date not supported")
        Date startingDate,

        @NotNull(message = "Loan duration can't be null")
        @Min(value = 1, message = "Loan duration must be at-least 1 month")
        Integer duration,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        @FutureOrPresent(message = "Past date not supported")
        Date endingDate,

        @NotEmpty(message = "Loan type can't be empty")
        String loanType,

        Boolean approvalStatus
) {

    public LoanDto(Loan loan) {
        this(loan.getId(), loan.getBorrower().getId(), loan.getAmount(), loan.getStartingDate(),
                loan.getDuration(), loan.getEndingDate(), loan.getLoanType(), loan.getApprovalStatus());
    }

    public static List<LoanDto> loanToDtoList(List<Loan> loanList) {
        List<LoanDto> loanDtoList = new ArrayList<>();
        for (Loan loan : loanList) {
            loanDtoList.add(new LoanDto(loan));
        }
        return loanDtoList;
    }

}
