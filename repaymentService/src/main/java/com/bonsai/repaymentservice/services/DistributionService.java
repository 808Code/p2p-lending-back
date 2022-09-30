package com.bonsai.repaymentservice.services;

import com.bonsai.repaymentservice.dto.Lending;

import java.math.BigDecimal;
import java.util.List;

public interface DistributionService {
    BigDecimal distribute(List<Lending> lendings, BigDecimal amount, int duration);
}
