package com.bonsai.repaymentservice.services;

import com.bonsai.repaymentservice.dto.EmailAmount;

import java.math.BigDecimal;
import java.util.List;

public interface DistributionService {
    void distribute(List<EmailAmount> lenders, BigDecimal amount);
}
