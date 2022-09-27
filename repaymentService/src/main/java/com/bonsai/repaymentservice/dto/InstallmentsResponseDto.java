package com.bonsai.repaymentservice.dto;

import com.bonsai.repaymentservice.models.Installment;

import java.util.List;

public record InstallmentsResponseDto(
        List<Installment> paidInstallment,
        List<Installment> upcomingInstallment,
        List<Installment> missedInstallment

) {}
