package com.bonsai.loansuggestionservice.wallet.repositories;

import com.bonsai.loansuggestionservice.wallet.models.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-08-06
 */
public interface WalletRepo extends JpaRepository<Wallet, UUID> {
    Wallet findByUserEmail(String email);

    @Query(nativeQuery = true, value = "select w.amount\n" +
            "from wallet w\n" +
            "inner join user_credential uc on uc.id = w.user_id\n" +
            "and uc.email = ?1")
    Long fetchBalanceFromWallet(String email);
}
