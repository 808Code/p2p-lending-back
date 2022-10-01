package com.bonsai.repaymentservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface Interest {
    LocalDateTime getDate();

    BigDecimal getAmount();
}
