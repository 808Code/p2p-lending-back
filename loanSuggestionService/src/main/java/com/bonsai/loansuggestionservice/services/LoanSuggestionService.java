package com.bonsai.loansuggestionservice.services;

import com.bonsai.accountservice.models.UserCredential;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-08-03
 */
public interface LoanSuggestionService {
    @Transactional
    Boolean save(UUID loanRequestId, List<UserCredential> lenders);
}
