package com.bonsai.repaymentservice.services;

import com.bonsai.repaymentservice.dto.GetLendingInterestsRequest;
import com.bonsai.repaymentservice.dto.Interest;

import java.util.List;

public interface InterestService {
    List<Interest> getInterests(GetLendingInterestsRequest getLendingInterestsRequest);
}
