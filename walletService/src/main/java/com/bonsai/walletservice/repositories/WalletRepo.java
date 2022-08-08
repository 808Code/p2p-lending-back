package com.bonsai.walletservice.repositories;

import com.bonsai.walletservice.models.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
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
    BigDecimal fetchBalanceFromWallet(String email);
}