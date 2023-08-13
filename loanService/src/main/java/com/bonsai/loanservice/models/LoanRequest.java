package com.bonsai.loanservice.models;

import com.bonsai.accountservice.models.UserCredential;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-05-25
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoanRequest {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "borrower_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_Loan_Borrower")
    )
    private UserCredential borrower;

    private Long amount;

    @Column(name = "requested_date")
    private LocalDate requestedDate;

    private Integer duration;

    @Column(name = "loan_type")
    private String loanType;

    @Column(name = "loan_status")
    private String loanStatus;

    @Column(name = "remaining_amount")
    private Long remainingAmount;

    @Column(name = "supporting_document")
    private String supportingDocumentPath;
    @Column(name = "loan_request_reason", columnDefinition = "TEXT")
    private String loanRequestReason;
}
