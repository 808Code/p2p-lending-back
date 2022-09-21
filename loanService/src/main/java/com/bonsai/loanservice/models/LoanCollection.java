package com.bonsai.loanservice.models;

import com.bonsai.accountservice.models.UserCredential;
import com.bonsai.walletservice.models.WalletTransaction;
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
    private LoanRequest loanRequest;

    @OneToOne
    @JoinColumn(name = "transaction_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_Loan_Collection_Wallet_Transaction"))
    private WalletTransaction walletTransaction;

    @OneToOne
    @JoinColumn(name = "lender_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_Loan_Collection_Lender"))
    private UserCredential lender;
}
