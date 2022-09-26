package com.bonsai.repaymentservice.services;

import com.bonsai.repaymentservice.dto.InstallmentDto;

import java.math.BigDecimal;

public interface InstallmentService {
    void saveInstallment(InstallmentDto installment);
    BigDecimal getEmi(long principal, int duration_in_months, float rate_yearly);
}
