package com.bonsai.loansuggestionservice.repositories;

import com.bonsai.loanservice.dto.LoanResponse;
import com.bonsai.loansuggestionservice.models.LoanSuggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-08-03
 */
public interface LoanSuggestionRepo extends JpaRepository<LoanSuggestion, UUID> {

    @Query(value = "select new com.bonsai.loanservice.dto.LoanResponse(lr)\n" +
            "from  LoanSuggestion ls\n" +
            "         inner join LoanRequest lr on lr.id = ls.loanRequest.id\n" +
            "         inner join UserCredential uc on uc.id = ls.lender.id\n" +
            "    and uc.email = ?1\n" +
            "where lr.approvalStatus = false\n" +
            "  and lower(lr.suggestionStatus) = 'new'")
    List<LoanResponse> findAllSuggestedLoansForLender(String email);
}
