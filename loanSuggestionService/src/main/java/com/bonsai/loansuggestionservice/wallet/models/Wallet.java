package com.bonsai.loansuggestionservice.wallet.models;

import com.bonsai.accountservice.models.UserCredential;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-08-06
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Wallet {
    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_Wallet_User"))
    private UserCredential user;

    private Long amount;
}
