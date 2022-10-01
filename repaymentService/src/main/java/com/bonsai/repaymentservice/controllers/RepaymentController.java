package com.bonsai.repaymentservice.controllers;


import com.bonsai.repaymentservice.dto.GetInstallmentsDto;
import com.bonsai.repaymentservice.dto.GetLendingInterestsRequest;
import com.bonsai.repaymentservice.dto.PayInstallmentRequest;
import com.bonsai.repaymentservice.services.InstallmentService;
import com.bonsai.repaymentservice.services.InterestService;
import com.bonsai.sharedservice.dtos.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
@CrossOrigin("*")
public class RepaymentController {

    private final InstallmentService installmentService;
    private final InterestService interestService;

    @PostMapping("/getInstallments")
    public ResponseEntity<SuccessResponse> getInstallments(@Valid @RequestBody GetInstallmentsDto getInstallmentsDto) {

        return ResponseEntity.ok(
                new SuccessResponse("Installments for the user fetched successfully.", installmentService.getInstallments(getInstallmentsDto))
        );
    }

    @PostMapping("/payInstallment")
    public ResponseEntity<SuccessResponse> payInstallment(@Valid @RequestBody PayInstallmentRequest payInstallmentRequest) {
        installmentService.payInstallment(payInstallmentRequest);
        return ResponseEntity.ok(
                new SuccessResponse("Installment Paid successfully.", true)
        );
    }

    @PostMapping("/getLendingInterests")
    public ResponseEntity<SuccessResponse> getLendingInterests(@Valid @RequestBody GetLendingInterestsRequest getLendingInterestsRequest) {
        return ResponseEntity.ok(
                new SuccessResponse("Interests Recieved", interestService.getInterests(getLendingInterestsRequest))
        );
    }


}
