package com.bonsai.repaymentservice.services;

import com.bonsai.repaymentservice.dto.GetInstallmentsDto;
import com.bonsai.repaymentservice.dto.InstallmentDto;
import com.bonsai.repaymentservice.dto.InstallmentsResponseDto;
import com.bonsai.repaymentservice.dto.PayInstallmentRequest;

import java.math.BigDecimal;

public interface InstallmentService {
    void saveInstallment(InstallmentDto installment);

    BigDecimal getEmi(long principal, int duration_in_months, float rate_yearly);

    void payInstallment(PayInstallmentRequest payInstallmentRequest);

    InstallmentsResponseDto getInstallments(GetInstallmentsDto getInstallmentsDto);

}
