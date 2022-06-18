package com.bonsai.accountservice.controllers.user;

import com.bonsai.accountservice.services.BorrowerService;
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
 * @since 2022-06-17
 */
@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class BorrowerController {

    private final BorrowerService borrowerService;

    @GetMapping("/borrower/isEligible/{borrowerEmail}")
    public ResponseEntity<SuccessResponse> isBorrowerEligible(@PathVariable String borrowerEmail) {
        return ResponseEntity.ok(
                new SuccessResponse("Borrower eligibility for loan fetched", borrowerService.isBorrowerEligibleForLoan(borrowerEmail))
        );
    }
}
