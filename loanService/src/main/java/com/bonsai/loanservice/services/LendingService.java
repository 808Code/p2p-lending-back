package com.bonsai.loanservice.services;

import com.bonsai.loanservice.dto.CreateLendingRequest;
import com.bonsai.loanservice.dto.LendingRequest;
import com.bonsai.loanservice.dto.LendingResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface LendingService {
    Long createLending(LendingRequest lendingRequest, String lenderEmail);

    Long lend(CreateLendingRequest createLendingRequest, String lenderEmail);

    UUID createLending(LocalDateTime lentDate, String lenderEmail, UUID loanRequestId, UUID transactionId);

    List<LendingResponse> fetchLendings(String lenderEmail);

    List<String> getAvailableLendingDurationList();
    Long getMaximumLendingAmount(Integer duration);
}
