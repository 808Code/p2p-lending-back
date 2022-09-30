package com.bonsai.repaymentservice.models;


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
public class InterestDistribution {
    @Id
    @GeneratedValue
    private UUID id;

    private UUID lendingId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_Transaction"))
    private WalletTransaction walletTransaction;


}
