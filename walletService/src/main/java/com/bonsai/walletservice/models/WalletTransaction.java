package com.bonsai.walletservice.models;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-08-06
 */
@Entity
@Table(name = "wallet_transaction")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WalletTransaction {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_Wallet_Transaction_Wallet"))
    private Wallet wallet;

    private BigDecimal amount;

    private String type;

    private String remarks;

    private LocalDateTime date;
}
