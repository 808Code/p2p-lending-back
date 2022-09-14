package com.bonsai.loanservice.repositories;

import com.bonsai.loanservice.models.LoanRequest;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
