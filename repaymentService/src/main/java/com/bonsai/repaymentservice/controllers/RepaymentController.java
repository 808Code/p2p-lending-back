package com.bonsai.repaymentservice.controllers;


import com.bonsai.repaymentservice.dto.GetInstallmentsDto;
import com.bonsai.repaymentservice.dto.PayInstallmentRequest;
import com.bonsai.repaymentservice.services.InstallmentService;
import com.bonsai.sharedservice.dtos.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class RepaymentController {

    private final InstallmentService installmentService;

    @PostMapping("/getInstallments")
    public ResponseEntity<SuccessResponse> getInstallments(@RequestBody GetInstallmentsDto getInstallmentsDto) {

        return ResponseEntity.ok(
                new SuccessResponse("Installments for the user fetched successfully.", installmentService.getInstallments(getInstallmentsDto))
        );
    }

    @PostMapping("/payInstallment")
    public ResponseEntity<SuccessResponse> payInstallment(@RequestBody PayInstallmentRequest payInstallmentRequest) {
        installmentService.payInstallment(payInstallmentRequest);
        return ResponseEntity.ok(
                new SuccessResponse("Installment Paid successfully.", true)
        );
    }


}
