package com.bonsai.repaymentservice.services.impl;

import com.bonsai.repaymentservice.dto.InstallmentDto;
import com.bonsai.repaymentservice.models.Installment;
import com.bonsai.repaymentservice.repositories.InstallmentRepo;
import com.bonsai.repaymentservice.services.InstallmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class InstallmentServiceImpl implements InstallmentService {

    private final InstallmentRepo installmentRepo;
    @Override
    public void saveInstallment(InstallmentDto installment) {
        installmentRepo.save(
                Installment.builder()
                        .amount(installment.amount())
                        .loanRequest(installment.loanId())
                        .status(installment.status())
                        .scheduledDate(installment.scheduledDate())
                        .installmentNumber(installment.installmentNumber())
                        .build()
        );

    }

    @Override
    public BigDecimal getEmi(long principal, int duration_in_months, float rate_yearly) {
        BigDecimal emi= BigDecimal.valueOf((principal * duration_in_months * 1.0 * rate_yearly /1200)+(principal * 1.0/ duration_in_months));
        return emi;


    }
}
