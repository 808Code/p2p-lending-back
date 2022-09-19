package com.bonsai.loanservice.models;

import com.bonsai.accountservice.models.UserCredential;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Lending {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "lender_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_Lending_Lender")
    )
    private UserCredential lender;

    @ManyToOne
    @JoinColumn(name = "loan_request_id)", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_Lending_Loan_Request")
    )
    private LoanRequest loanRequest;

    private Long amount;

    @Column(name = "requested_date")
    private LocalDate lendingDate;

}
