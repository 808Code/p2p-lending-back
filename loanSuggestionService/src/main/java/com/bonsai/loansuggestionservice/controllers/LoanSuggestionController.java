package com.bonsai.loansuggestionservice.controllers;

import com.bonsai.loansuggestionservice.services.LoanSuggestionService;
import com.bonsai.sharedservice.dtos.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-08-03
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class LoanSuggestionController {

    private final LoanSuggestionService loanSuggestionService;

    @GetMapping("/getLoanSuggestionsForLender/{email}")
    public ResponseEntity<SuccessResponse> findAllSuggestedLoansForLender(@PathVariable("email") String email) {
        return ResponseEntity.ok(new SuccessResponse("Suggested loans for borrower fetched successfully",
                loanSuggestionService.findSuggestedLoansForLender(email)));
    }
}
