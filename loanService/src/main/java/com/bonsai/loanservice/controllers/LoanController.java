package com.bonsai.loanservice.controllers;

import com.bonsai.sharedservice.dtos.response.SuccessResponse;
import com.bonsai.loanservice.dto.LoanRequestDto;
import com.bonsai.loanservice.services.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-05-25
 */
@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @PostMapping("/createLoan")
    public ResponseEntity<SuccessResponse> createLoan(@Valid @RequestBody LoanRequestDto loanRequestDto) {
        return ResponseEntity.ok(
                new SuccessResponse("Loan request created successfully", loanService.save(loanRequestDto))
        );
    }

    @GetMapping("/borrower/findAllLoan/{borrowerEmail}")
    public ResponseEntity<SuccessResponse> findAllBorrowerLoan(@PathVariable String borrowerEmail) {
        return ResponseEntity.ok(
                new SuccessResponse("Loan request list fetched successfully", loanService.findAllByBorrower(borrowerEmail))
        );
    }

    @GetMapping("/getAllLoanTypes")
    public ResponseEntity<SuccessResponse> findAllLoanTypes() {
        return ResponseEntity.ok(
                new SuccessResponse("Loan type list fetched successfully", loanService.findAllLoanTypes())
        );
    }
}
