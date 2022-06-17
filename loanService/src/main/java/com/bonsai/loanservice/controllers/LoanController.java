package com.bonsai.loanservice.controllers;

import com.bonsai.sharedservice.dtos.response.SuccessResponse;
import com.bonsai.loanservice.dto.LoanRequestDto;
import com.bonsai.loanservice.services.LoanService;
import com.bonsai.sharedservice.exceptions.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.UUID;

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
    public ResponseEntity<SuccessResponse> createLoan(@Valid @RequestBody LoanRequestDto loanRequestDto, BindingResult bindingResult) throws ParseException {
        if(bindingResult.hasErrors()) {
            throw new AppException(bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(
                new SuccessResponse("Loan request created successfully", loanService.save(loanRequestDto))
        );
    }

    @GetMapping("/borrower/findAllLoan/{borrowerId}")
    public ResponseEntity<SuccessResponse> findAllBorrowerLoan(@PathVariable UUID borrowerId) {
        return ResponseEntity.ok(
                new SuccessResponse("Loan request list fetched successfully", loanService.findAllByBorrower(borrowerId))
        );
    }
}
