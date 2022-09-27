package com.bonsai.loanservice.dto;

import com.bonsai.loanservice.models.Lending;
import com.bonsai.loanservice.models.LoanCollection;

import java.time.format.DateTimeFormatter;

public record LendingResponse(
        String transactionId,
        String loanRequestId,
        String lentDate,
        String lentTime,
        Long amount,
        String status
) {
    public LendingResponse(Lending lending) {
        this(
                lending.getTransaction().getId().toString(),
                lending.getLoanRequest().getId().toString(),
                lending.getTransaction().getDate().format(DateTimeFormatter.ofPattern("YYYY-MM-dd")),
                lending.getTransaction().getDate().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                lending.getTransaction().getAmount().longValue(),
                "DISBURSED"
        );
    }

    public LendingResponse(LoanCollection collection) {
        this(
                collection.getWalletTransaction().getId().toString(),
                collection.getLoanRequest().getId().toString(),
                collection.getWalletTransaction().getDate().format(DateTimeFormatter.ofPattern("YYYY-MM-dd")),
                collection.getWalletTransaction().getDate().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                collection.getWalletTransaction().getAmount().longValue(),
                "POOLED"
        );
    }
}
