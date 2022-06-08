package com.bonsai.accountservice.models;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;


@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Finance {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    String grossAnnualIncome;

    @Column(nullable = false)
    String monthlySalary;

    @Column(nullable = false)
    String rent;

    @Column(nullable = false)
    String educationalExpenses;

    @Column(nullable = false)
    String houseHoldExpenses;

    @Column(nullable = false)
    String monthlyPersonalExpenses;

    @Column(nullable = false)
    String monthlySavings;

}
