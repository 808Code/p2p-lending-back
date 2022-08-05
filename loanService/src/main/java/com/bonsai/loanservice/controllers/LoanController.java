package com.bonsai.loanservice.controllers;

import com.bonsai.loanservice.dto.LoanResponse;
import com.bonsai.sharedservice.dtos.loan.LoanInQueue;
import com.bonsai.sharedservice.dtos.response.SuccessResponse;
import com.bonsai.loanservice.dto.LoanRequestDto;
import com.bonsai.loanservice.services.LoanService;
import com.bonsai.sharedservice.rmq.MessagingConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
@CrossOrigin("*")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;
    private final RabbitTemplate rabbitTemplate;

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
}
