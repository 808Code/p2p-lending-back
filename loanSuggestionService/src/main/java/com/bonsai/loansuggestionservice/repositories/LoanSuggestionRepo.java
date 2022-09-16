package com.bonsai.loansuggestionservice.repositories;

import com.bonsai.loansuggestionservice.models.LoanSuggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-08-03
 */
public interface LoanSuggestionRepo extends JpaRepository<LoanSuggestion, UUID> {

    @Query(nativeQuery = true, value = """
            select cast(lr.id as varchar) as "loanId",
                   lr.amount              as "loanAmount",
                   lr.loan_type           as "loanType",
                   lr.remaining_amount    as "remainingAmount",
                   (case
                        when lr.remaining_amount >= ?3
                            then ?3
                        else lr.remaining_amount
                       end)               as "maxLendingAmount",
                   (
                       case
                           when lr.remaining_amount < ?2
                               then 0
                           else ?2
                           end
                       )                  as "minLendingAmount"
            from loan_suggestion ls
                     inner join loan_request lr on lr.id = ls.loan_request_id
                     inner join user_credential uc on uc.id = ls.lender_id
                and uc.email = ?1
            where lr.loan_status in (upper('new'), upper('suggested'))
                        """)
    List<Map<String, Object>> findAllSuggestedLoansForLender(String email, Long minLendingAmount, Long maxLendingAmount);
}
