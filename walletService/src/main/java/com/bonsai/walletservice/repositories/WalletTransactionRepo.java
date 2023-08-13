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

    @Query(nativeQuery = true, value = """
      SELECT
      CAST(uc.id AS VARCHAR) AS lender_id,
      CAST(l.loan_request_id AS VARCHAR) AS loan_id,
      CONCAT(k.first_name, ' ', k.last_name) AS borrower_name,
      wt.amount AS amount_invested,
      TO_CHAR(wt.date, 'YYYY-MM-DD') || ' at ' || TO_CHAR(wt.date, 'HH:MI am') AS disbursement_date,
      '15%' AS interest_rate,
      CONCAT(lr.duration, ' months') AS duration,
      wt1.amount AS emi_received
  FROM
      user_credential uc
          INNER JOIN lending l ON uc.id = l.lender_id
          INNER JOIN interest_distribution id ON l.id = id.lending_id
          INNER JOIN wallet_transaction wt1 ON id.transaction_id = wt1.id
          INNER JOIN wallet_transaction wt ON l.transaction_id = wt.id
          INNER JOIN loan_request lr ON l.loan_request_id = lr.id
          INNER JOIN user_credential uc2 ON uc2.id = lr.borrower_id
          INNER JOIN kyc k ON uc2.kyc_id = k.id
  WHERE
          uc.email = ?1""")
    List<Map<String, Object>> getBorrowerTranReport(String user);

}
