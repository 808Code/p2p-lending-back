package com.bonsai.loanservice.services;

import com.bonsai.loanservice.dto.LendRequest;
import com.bonsai.loanservice.dto.LendingResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface LendingService {
    Long lend(LendRequest lendRequest, String lenderEmail);
    UUID createLending(LocalDateTime lentDate, String lenderEmail, UUID loanRequestId, UUID transactionId);
    List<LendingResponse> fetchLendings(String lenderEmail);
}
