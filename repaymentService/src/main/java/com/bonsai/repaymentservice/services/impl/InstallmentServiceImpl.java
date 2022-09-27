package com.bonsai.repaymentservice.services.impl;

import com.bonsai.repaymentservice.constants.InstallmentStatus;
import com.bonsai.repaymentservice.dto.GetInstallmentsDto;
import com.bonsai.repaymentservice.dto.InstallmentDto;
import com.bonsai.repaymentservice.dto.InstallmentsResponseDto;
import com.bonsai.repaymentservice.dto.PayInstallmentRequest;
import com.bonsai.repaymentservice.models.Installment;
import com.bonsai.repaymentservice.repositories.InstallmentRepo;
import com.bonsai.repaymentservice.services.InstallmentService;
import com.bonsai.sharedservice.exceptions.AppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class InstallmentServiceImpl implements InstallmentService {

    private final InstallmentRepo installmentRepo;

    @Override
    public void saveInstallment(InstallmentDto installment) {
        installmentRepo.save(
                Installment.builder()
                        .amount(installment.amount())
                        .loanRequest(installment.loanId())
                        .status(installment.status())
                        .scheduledDate(installment.scheduledDate())
                        .installmentNumber(installment.installmentNumber())
                        .build()
        );

    }

    @Override
    public BigDecimal getEmi(long principal, int duration_in_months, float rate_yearly) {
        BigDecimal emi = BigDecimal.valueOf((principal * duration_in_months * 1.0 * rate_yearly / 1200) + (principal * 1.0 / duration_in_months));
        return emi;


    }

    @Override
    public void payInstallment(PayInstallmentRequest payInstallmentRequest) {
        //check if amount
        //debit wallet
        Installment installment = installmentRepo.findById(UUID.fromString(payInstallmentRequest.installmentId()))
                .orElseThrow(() -> new AppException("Invalid Installment Id", HttpStatus.BAD_REQUEST));
        log.info("The installment{}", installment);
        installment.setStatus(InstallmentStatus.PAID);
        installment.setPaidDate(LocalDate.now());
        installmentRepo.save(installment);

    }

    @Override
    public InstallmentsResponseDto getInstallments(GetInstallmentsDto getInstallmentsDto) {
        List<Installment> installments = installmentRepo.findByLoanRequest(UUID.fromString(getInstallmentsDto.loanId()));
        List<Installment> paidInstallment = new ArrayList<>();
        List<Installment> unpaidInstallment = new ArrayList<>();
        List<Installment> missedInstallment = new ArrayList<>();
        for (Installment installment : installments) {
            if (Objects.equals(installment.getStatus(), InstallmentStatus.PAID)) {
                paidInstallment.add(installment);
            } else {
                if ((installment.getScheduledDate()).isBefore(LocalDate.now())) {
                    missedInstallment.add(installment);
                }else{
                    unpaidInstallment.add(installment);
                }

            }
        }


        InstallmentsResponseDto installmentsResponseDto = new InstallmentsResponseDto(paidInstallment, unpaidInstallment, missedInstallment);
        return installmentsResponseDto;
    }


}
