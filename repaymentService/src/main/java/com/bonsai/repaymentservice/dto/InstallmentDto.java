package com.bonsai.repaymentservice.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record InstallmentDto(
        UUID loanId,
        int installmentNumber,
        BigDecimal amount,
        LocalDate scheduledDate,
        String status

){}
