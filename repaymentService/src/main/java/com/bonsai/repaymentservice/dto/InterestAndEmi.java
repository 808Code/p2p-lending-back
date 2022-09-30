package com.bonsai.repaymentservice.dto;

import java.math.BigDecimal;

public record InterestAndEmi(
        BigDecimal interest,
        BigDecimal emi
) {
}
