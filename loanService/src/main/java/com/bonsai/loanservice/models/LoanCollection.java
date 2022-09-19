package com.bonsai.loanservice.models;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoanCollection {

    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne
    @JoinColumn(name = "loan_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_Loan_Collection_Loan"))
    LoanRequest loanRequest;

    @Column(name = "collected_amount")
    private Long collectedAmount;
}
