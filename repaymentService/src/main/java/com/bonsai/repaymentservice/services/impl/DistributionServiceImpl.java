package com.bonsai.repaymentservice.services.impl;

import com.bonsai.repaymentservice.dto.EmailAmount;
import com.bonsai.repaymentservice.services.DistributionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DistributionServiceImpl implements DistributionService {


    @Override
    public void distribute(List<EmailAmount> lenders, BigDecimal amount) {
        lenders.forEach(lender ->log.info("{}{}",lender.getEmail(),lender.getAmount()));
    }
}
