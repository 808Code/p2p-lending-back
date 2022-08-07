package com.bonsai.loansuggestionservice.wallet.repositories;

import com.bonsai.loansuggestionservice.wallet.models.WalletTransaction;
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

    @Query(nativeQuery = true, value = "select cast(w.id as varchar),\n" +
            "       w.amount,\n" +
            "       date(w.date) as date,\n" +
            "       cast(w.date as time) as time,\n" +
            "       w.remarks    as remarks,\n" +
            "       w.type       as type\n" +
            "from wallet_transaction w\n" +
            "         inner join wallet w2 on w.wallet_id = w2.id\n" +
            "         inner join user_credential uc on w2.user_id = uc.id\n" +
            "    and uc.email = ?1")
    List<Map<String, Object>> findAllTransactionsByUserEmail(String email);
}
