package com.bonsai.repaymentservice.models;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlatformEarning {
    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "installment_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_Installment"))
    private Installment installment;

    private BigDecimal amount;
}
