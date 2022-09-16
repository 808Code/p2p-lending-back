package com.bonsai.loanservice.dto;

import com.bonsai.loanservice.models.LoanRequest;

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
        String borrower,
        Long amount,
        String requestedDate,
        Integer duration,
        String loanType,
        String loanStatus
) {
    public LoanResponse(LoanRequest loanRequest) {
        this(loanRequest.getId(), loanRequest.getBorrower().getEmail(), loanRequest.getAmount(), loanRequest.getRequestedDate().toString(),
                loanRequest.getDuration(), loanRequest.getLoanType(), loanRequest.getLoanStatus());
    }

    public static List<LoanResponse> loanToDtoList(List<LoanRequest> loanList) {
        return loanList.stream().map(
                loanRequest -> new LoanResponse(loanRequest)
        ).collect(Collectors.toList());
    }
}
