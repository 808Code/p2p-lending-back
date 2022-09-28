package com.bonsai.loanservice.repositories;

import com.bonsai.loanservice.models.LoanRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-05-25
 */
public interface LoanRequestRepo extends JpaRepository<LoanRequest, UUID> {

    @Query(nativeQuery = true, value = """
            select lr.*
            from user_credential u
                     inner join loan_request lr on u.id = lr.borrower_id
            where u.email = ?1
              and u.role = 'BORROWER'
            order by lr.requested_date""")
    List<LoanRequest> findAllByBorrower(String email);

    @Modifying
    @Query(nativeQuery = true, value = """
            delete
            from loan_suggestion ls
            where loan_request_id = ?1
              and lender_id = (select u.id
                               from user_credential u
                               where u.email = ?2
                                 and u.role = 'LENDER'
                               limit 1)
            """)
    void clearLoanSuggestion(UUID loanId, String lenderEmail);
}
