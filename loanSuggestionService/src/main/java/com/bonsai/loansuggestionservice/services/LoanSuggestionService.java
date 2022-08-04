package com.bonsai.loansuggestionservice.services;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-08-03
 */
public interface LoanSuggestionService {
    @Transactional
    void save(UUID loanRequestId);

    List<Map<String, Object>> findSuggestedLoansForLender(String email);
}
