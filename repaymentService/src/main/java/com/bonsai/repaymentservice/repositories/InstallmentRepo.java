package com.bonsai.repaymentservice.repositories;

import com.bonsai.repaymentservice.dto.EmailAmount;
import com.bonsai.repaymentservice.models.Installment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InstallmentRepo extends JpaRepository<Installment, UUID> {
    List<Installment> findAllByLoanRequest(UUID loanRequestId);

    @Query(nativeQuery = true, value = """
            select email,amount 
            from user_credential 
            INNER JOIN(SELECT  lender_id,amount,loan_request_id FROM lending l
            INNER JOIN wallet_transaction wt on l.transaction_id = wt.id)lt on user_credential.id=lt.lender_id where loan_request_id = ?1  
            """)
    List<EmailAmount> fetchAllByLoanRequestIdTheEmailAndAmount(UUID loanRequestId);
}
