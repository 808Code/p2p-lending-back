package com.bonsai.loanservice.services;

import com.bonsai.loanservice.dto.LendRequest;

public interface LendingService {
    Long lend(LendRequest lendRequest, String lenderEmail);
}
