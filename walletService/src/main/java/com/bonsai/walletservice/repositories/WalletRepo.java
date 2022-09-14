package com.bonsai.walletservice.repositories;

import com.bonsai.walletservice.models.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-08-06
 */
public interface WalletRepo extends JpaRepository<Wallet, UUID> {
    Wallet findByUserEmail(String email);

    @Query(nativeQuery = true, value = """
            select w.amount as "toatalBalance",
                   (w.amount - coalesce(a."lockedBalance", 0)) as "availableBalance"
            from wallet w
                     inner join user_credential uc on uc.id = w.user_id and uc.email = ?1
                     left join (select wt.wallet_id,
                                       sum(wt.amount) as "lockedBalance"
                                from wallet_transaction wt
                                         inner join wallet w on w.id = wt.wallet_id
                                         inner join user_credential u on w.user_id = u.id and u.email = ?1
                                where lower(wt.type) = 'locked'
                                group by wt.wallet_id) a on w.id = a.wallet_id""")
    Map<String, BigDecimal> fetchBalanceFromWallet(String email);
}
