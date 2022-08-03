package com.bonsai.loansuggestionservice.models;

import com.bonsai.accountservice.models.UserCredential;
import com.bonsai.loanservice.models.LoanRequest;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-08-03
 */
@Entity
@Table(name = "loan_suggestion",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"loan_request_id", "lender_id"},
                name = "UK_Lender_Loan"
        ))
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoanSuggestion {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_request_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_Loan_Suggestion_Loan_Request")
    )
    private LoanRequest loanRequest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lender_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_Loan_Suggestion_Lender")
    )
    private UserCredential lender;
}
