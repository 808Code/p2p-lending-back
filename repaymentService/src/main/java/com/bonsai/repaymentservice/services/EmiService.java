package com.bonsai.repaymentservice.services;

import com.bonsai.repaymentservice.dto.InterestAndEmi;

public interface EmiService {
    public InterestAndEmi calculateMonthlyInterestAndEMI(long principal, int durationInMonths, float rateYearly);
}
