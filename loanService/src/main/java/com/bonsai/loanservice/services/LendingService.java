package com.bonsai.loanservice.services;

import com.bonsai.loanservice.dto.LendRequest;

import java.time.LocalDateTime;
import java.util.UUID;

public interface LendingService {
    Long lend(LendRequest lendRequest, String lenderEmail);
    UUID createLending(LocalDateTime lentDate, String lenderEmail, UUID loanRequestId, UUID transactionId);
}
