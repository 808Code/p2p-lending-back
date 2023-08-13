
package com.bonsai.loanservice.dto;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
        Long amount,

        @NotNull(message = "Loan duration can't be null")
        @Min(value = 1, message = "Loan duration must be at-least 1 month")
        Integer duration,

        @NotEmpty(message = "Loan type can't be empty")
        String loanType,

        String supportingDocumentPath,
        String message,
        @NotNull(message = "Supporting Document Cannot Be Null")
        MultipartFile supportingDoc
) {

}