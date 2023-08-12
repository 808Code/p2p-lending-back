package com.bonsai.loanservice.repositories;

import com.bonsai.loanservice.models.LoanRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
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

    @Query(nativeQuery = true, value = """
            select lr.*
            from loan_request lr
                     left join loan_collection lc on lr.id = lc.loan_id
            where (lc.lender_id <> ?1 or lc.lender_id is null)
              and lr.duration = ?2
              and lr.remaining_amount >= ?3
              and lr.loan_status = 'NEW'
            order by lr.requested_date asc
            limit 1
                   """)
    Optional<LoanRequest> findLendableLoanRequest(String lenderId, Integer duration, Long remainingAmount);

    @Query(nativeQuery = true, value = """
            select distinct lr.duration
            from loan_request lr
                     left join loan_collection lc on lr.id = lc.loan_id
            where lr.loan_status = 'NEW'
              and (lc.lender_id <> ?1 or lc.lender_id is null)
            order by lr.duration asc
                                    """)
    List<Integer> getAvailableLendingDurationList(String lenderId);

    @Query(nativeQuery = true, value = """
            select max(lr.remaining_amount)
            from loan_request lr
                     left join loan_collection lc on lr.id = lc.loan_id
            where (lc.lender_id <> ?1 or lc.lender_id is null)
              and lr.duration = ?2
              and lr.loan_status = 'NEW'
                        """)
    Long getMaximumRemainingLoanRequestAmountByDuration(String lenderId, Integer duration);
}
