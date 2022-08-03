package com.bonsai.loansuggestionservice.repositories;

import com.bonsai.loansuggestionservice.models.LoanSuggestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-08-03
 */
public interface LoanSuggestionRepo extends JpaRepository<LoanSuggestion, UUID> {
}
