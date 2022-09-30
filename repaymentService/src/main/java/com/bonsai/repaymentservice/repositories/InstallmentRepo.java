package com.bonsai.repaymentservice.repositories;

import com.bonsai.repaymentservice.dto.Lending;
import com.bonsai.repaymentservice.models.Installment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InstallmentRepo extends JpaRepository<Installment, UUID> {
    List<Installment> findAllByLoanRequest(UUID loanRequestId);

    @Query(nativeQuery = true, value = """
        select email, amount, CAST(lender_id as varchar) id
        from user_credential
                 INNER JOIN(SELECT lender_id, amount, loan_request_id, l.id as lending_id
                            FROM lending l
                                     INNER JOIN wallet_transaction wt on l.transaction_id = wt.id) lt
                           on user_credential.id = lt.lender_id
        where loan_request_id = ?1
         """)
    List<Lending> fetchAllByLoanRequestIdTheEmailAndAmount(UUID loanRequestId);
}
