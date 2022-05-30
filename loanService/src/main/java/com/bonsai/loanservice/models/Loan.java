package com.bonsai.loanservice.models;

import com.bonsai.accountservice.models.UserCredential;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

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
public class Loan {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "borrower_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_Loan_Borrower")
    )
    private UserCredential borrower;

    private Long amount;

    @Column(name = "approval_status")
    private Boolean approvalStatus;

    @Column(name = "requested_date")
    private LocalDate requestedDate;

    private Integer duration;

    private LocalDate deadline;

    @Column(name = "loan_type")
    private String loanType;
}
