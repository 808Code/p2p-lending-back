package com.bonsai.repaymentservice.repositories;

import com.bonsai.repaymentservice.dto.Interest;
import com.bonsai.repaymentservice.dto.Lending;
import com.bonsai.repaymentservice.models.Installment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Transactional
@Repository
public interface InstallmentRepo extends JpaRepository<Installment, UUID> {
    @Query(nativeQuery = true, value = """
            select *
            from installment i
            where loan_request = ?1
            order by scheduled_date desc
            """)
    List<Installment> findAllByLoanRequest(UUID loanRequestId);

    @Query(nativeQuery = true, value = """
            select email, amount, CAST(lending_id as varchar) id
            from user_credential
                     INNER JOIN(SELECT lender_id, amount, loan_request_id, l.id as lending_id
                                FROM lending l
                                         INNER JOIN wallet_transaction wt on l.transaction_id = wt.id) lt
                               on user_credential.id = lt.lender_id
            where loan_request_id = ?1
             """)
    List<Lending> fetchAllByLoanRequestIdTheEmailAndAmount(UUID loanRequestId);

    @Query(nativeQuery = true, value = """
            SELECT date,amount
            FROM interest_distribution int_d
                     INNER JOIN wallet_transaction wt
                                on int_d.transaction_id = wt.id
            WHERE int_d.lending_id = ?1
            ORDER BY date DESC
            """)
    List<Interest> fetchAllByInterestsByLendingId(UUID lendingId);

    @Modifying
    @Query(nativeQuery = true, value = """
            UPDATE loan_request SET loan_status= ?2 WHERE id = ?1                     
              """)
    void makeLoanCompletedByLoanID(UUID loanId, String loanStatus);

}
