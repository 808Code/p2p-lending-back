package com.bonsai.loanservice.controllers;

import com.bonsai.loanservice.dto.LendingRequest;
import com.bonsai.loanservice.dto.LoanRequestDto;
import com.bonsai.loanservice.dto.LoanResponse;
import com.bonsai.loanservice.services.LendingService;
import com.bonsai.loanservice.services.LoanService;
import com.bonsai.sharedservice.dtos.loan.LoanInQueue;
import com.bonsai.sharedservice.dtos.response.SuccessResponse;
import com.bonsai.sharedservice.rmq.MessagingConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-05-25
 */
@RestController
@RequestMapping("/api/v1/")
@CrossOrigin("*")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;
    private final RabbitTemplate rabbitTemplate;
    private final LendingService lendingService;

    @PostMapping("/createLoan")
    public ResponseEntity<SuccessResponse> createLoan(@Valid @RequestBody LoanRequestDto loanRequestDto) {
        //save the loan request to database
        LoanResponse loanResponse = loanService.save(loanRequestDto);

        //push the created loan to queue
        LoanInQueue loanInQueue = new LoanInQueue(loanResponse.id(), loanResponse.borrower());
        rabbitTemplate.convertAndSend(MessagingConfig.EXCHANGE, MessagingConfig.ROUTING_KEY, loanInQueue);

        //return response
        return ResponseEntity.ok(
                new SuccessResponse("Loan request created successfully", loanResponse)
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

    @PostMapping("/lend")
    public ResponseEntity<SuccessResponse> lend(@Valid @RequestBody LendingRequest lendingRequest, Authentication authentication) {
        String user = (String) authentication.getPrincipal();
        return ResponseEntity.ok(
                new SuccessResponse("Amount lending successful", lendingService.createLending(lendingRequest, user))
        );
    }

    @GetMapping("/getAvailableLendingDurationList")
    public ResponseEntity<SuccessResponse> getAvailableLendingDuration() {
        return ResponseEntity.ok(
                new SuccessResponse("Available lending duration list fetched successfully", lendingService.getAvailableLendingDurationList())
        );
    }

    @GetMapping("/getMaximumLendingAmount")
    public ResponseEntity<SuccessResponse> getMaximumLendingAmount(@RequestParam(value = "duration") Integer duration) {
        return ResponseEntity.ok(
                new SuccessResponse("Maximum Lending Amount fetched successfully", lendingService.getMaximumLendingAmount(duration))
        );
    }
}
