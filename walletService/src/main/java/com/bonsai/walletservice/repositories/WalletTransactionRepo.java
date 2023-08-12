package com.bonsai.walletservice.repositories;

import com.bonsai.walletservice.models.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-08-06
 */
public interface WalletTransactionRepo extends JpaRepository<WalletTransaction, UUID> {

    @Query(nativeQuery = true, value = """
            select cast(w.id as varchar),
                   w.amount,
                   w.date as date,
                   w.remarks    as remarks,
                   w.type       as type
            from wallet_transaction w
                     inner join wallet w2 on w.wallet_id = w2.id
                     inner join user_credential uc on w2.user_id = uc.id
                and uc.email = ?1
            order by w.date desc""")
    List<Map<String, Object>> findAllTransactionsByUserEmail(String email);
}
