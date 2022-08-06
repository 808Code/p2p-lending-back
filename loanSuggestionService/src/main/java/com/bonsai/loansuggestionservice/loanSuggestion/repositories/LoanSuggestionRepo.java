package com.bonsai.loansuggestionservice.loanSuggestion.repositories;

import com.bonsai.loansuggestionservice.loanSuggestion.models.LoanSuggestion;
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

    @Query(nativeQuery = true, value = "select cast(lr.id as varchar) as \"loanId\",\n" +
            "       lr.amount              as \"loanAmount\",\n" +
            "       lr.loan_type           as \"loanType\",\n" +
            "       lr.remaining_amount    as \"remainingAmount\",\n" +
            "       (case\n" +
            "            when lr.remaining_amount >= ?3\n" +
            "                then ?3\n" +
            "            else lr.remaining_amount\n" +
            "           end)               as \"maxLendingAmount\",\n" +
            "       (\n" +
            "           case\n" +
            "               when lr.remaining_amount < ?2\n" +
            "                   then 0\n" +
            "               else ?2\n" +
            "               end\n" +
            "           )                  as \"minLendingAmount\"\n" +
            "from loan_suggestion ls\n" +
            "         inner join loan_request lr on lr.id = ls.loan_request_id\n" +
            "         inner join user_credential uc on uc.id = ls.lender_id\n" +
            "    and uc.email = ?1\n" +
            "where lr.approval_status = false\n" )
    List<Map<String, Object>> findAllSuggestedLoansForLender(String email, Long minLendingAmount, Long maxLendingAmount);
}
