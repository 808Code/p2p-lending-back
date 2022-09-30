package com.bonsai.repaymentservice.services.impl;

import com.bonsai.repaymentservice.dto.InterestAndEmi;
import com.bonsai.repaymentservice.services.EmiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class EmiServiceImpl implements EmiService {

    @Override
    public InterestAndEmi calculateMonthlyInterestAndEMI(long principal, int durationInMonths, float rateYearly) {
        double monthlyInterest = principal * 1.0 * rateYearly / 1200;
        double monthlyPrincipal = principal * 1.0 / durationInMonths;
        return new InterestAndEmi(BigDecimal.valueOf(monthlyInterest), BigDecimal.valueOf(monthlyInterest + monthlyPrincipal));
    }
}

