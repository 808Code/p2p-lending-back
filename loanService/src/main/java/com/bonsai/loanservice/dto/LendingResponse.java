package com.bonsai.loanservice.dto;

import com.bonsai.loanservice.models.Lending;
import com.bonsai.loanservice.models.LoanCollection;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class LendingResponse {
    private String transactionId;
    private String loanRequestId;
    private String lentDate;
    private String lentTime;
    private Long amount;
    private String status;

    public LendingResponse(Lending lending) {
        this.transactionId = lending.getTransaction().getId().toString();
        this.loanRequestId = lending.getLoanRequest().getId().toString();
        this.lentDate = lending.getTransaction().getDate().format(DateTimeFormatter.ofPattern("YYYY-MM-dd"));
        this.lentTime = lending.getTransaction().getDate().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        this.amount = lending.getTransaction().getAmount().longValue();
        this.status = "DISBURSED";
    }

    public LendingResponse(LoanCollection collection) {
        this.transactionId = collection.getWalletTransaction().getId().toString();
        this.loanRequestId = collection.getLoanRequest().getId().toString();
        this.lentDate = collection.getWalletTransaction().getDate().format(DateTimeFormatter.ofPattern("YYYY-MM-dd"));
        this.lentTime = collection.getWalletTransaction().getDate().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        this.amount = collection.getWalletTransaction().getAmount().longValue();
        this.status = "POOLED";
    }
}
