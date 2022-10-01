package com.bonsai.repaymentservice.services.impl;

import com.bonsai.repaymentservice.dto.GetLendingInterestsRequest;
import com.bonsai.repaymentservice.dto.Interest;
import com.bonsai.repaymentservice.repositories.InstallmentRepo;
import com.bonsai.repaymentservice.services.InterestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class InterestServiceImpl implements InterestService {
    private final InstallmentRepo installmentRepo;

    @Override
    public List<Interest> getInterests(GetLendingInterestsRequest getLendingInterestsRequest) {
        List<Interest> interests = installmentRepo.fetchAllByInterestsByLendingId(UUID.fromString(getLendingInterestsRequest.lendingId()));
        return interests;
    }
}
