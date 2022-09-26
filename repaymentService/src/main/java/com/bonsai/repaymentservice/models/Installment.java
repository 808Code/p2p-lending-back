package com.bonsai.repaymentservice.models;


import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Installment {
    @Id
    @GeneratedValue
    private UUID id;

    private UUID loanRequest;

    private BigDecimal amount;

    private String status;

    private int installmentNumber;

    @Column(name = "scheduled_date")
    private LocalDate scheduledDate;

    @Column(name = "paid_date")
    private LocalDate paidDate;




}
