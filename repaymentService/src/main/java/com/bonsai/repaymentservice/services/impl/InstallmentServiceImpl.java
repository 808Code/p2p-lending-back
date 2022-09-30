package com.bonsai.repaymentservice.services.impl;

import com.bonsai.accountservice.models.UserCredential;
import com.bonsai.accountservice.repositories.UserCredentialRepo;
import com.bonsai.repaymentservice.constants.InstallmentStatus;
import com.bonsai.repaymentservice.dto.*;
import com.bonsai.repaymentservice.models.Installment;
import com.bonsai.repaymentservice.models.PlatformEarning;
import com.bonsai.repaymentservice.repositories.InstallmentRepo;
import com.bonsai.repaymentservice.repositories.InterestDistributionRepo;
import com.bonsai.repaymentservice.repositories.PlatformEarningRepo;
import com.bonsai.repaymentservice.services.DistributionService;
import com.bonsai.repaymentservice.services.InstallmentService;
import com.bonsai.sharedservice.exceptions.AppException;
import com.bonsai.walletservice.constants.WalletTransactionTypes;
import com.bonsai.walletservice.services.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class InstallmentServiceImpl implements InstallmentService {

    private final InstallmentRepo installmentRepo;
    private final WalletService walletService;
    private final UserCredentialRepo userCredentialRepo;
    private final DistributionService distributionService;
    private final PlatformEarningRepo platformEarningRepo;


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
    public BigDecimal calculateMonthlyEMI(long principal, int durationInMonths, float rateYearly) {
        double monthlyInterest =  principal * 1.0 * rateYearly / 1200;
        double monthlyPrincipal = principal * 1.0 / durationInMonths;

        return BigDecimal.valueOf(monthlyInterest + monthlyPrincipal);


    }

    @Override
    public void payInstallment(PayInstallmentRequest payInstallmentRequest) {
        List<Installment> installments = installmentRepo.findAllByLoanRequest(UUID.fromString(payInstallmentRequest.loanId()));

        Iterator<Installment> installmentIterator = installments.iterator();
        Installment installment = null;
        int paidCounter = 0;
        while (installmentIterator.hasNext()) {
            Installment tempInstallment = installmentIterator.next();
            if (tempInstallment.getId().equals(UUID.fromString(payInstallmentRequest.installmentId()))) {
                installment = tempInstallment;
            }
            if (tempInstallment.getStatus().equals(InstallmentStatus.PAID)) {
                paidCounter++;
            }
        }
        if (installment == null) {
            throw new AppException("Invalid Installment payment request", HttpStatus.BAD_REQUEST);
        }

        walletService.debitOrLockAmount(WalletTransactionTypes.DEBIT, installment.getAmount(), payInstallmentRequest.email());
        installment.setStatus(InstallmentStatus.PAID);
        installment.setPaidDate(LocalDate.now());
        installmentRepo.save(installment);
        List<Lending> lendings = installmentRepo.fetchAllByLoanRequestIdTheEmailAndAmount(UUID.fromString(payInstallmentRequest.loanId()));

        //remaining amount only from emi not principal
        BigDecimal remainingAmount = distributionService.distribute(
                lendings,
                installment.getAmount(),
                installments.size()
        );
        platformEarningRepo.save(
                PlatformEarning.builder()
                        .amount(remainingAmount)
                        .installment(installment)
                        .build()
        );

        if (installments.size() - paidCounter == 1) {
            UserCredential userCredential = userCredentialRepo.findByEmail(payInstallmentRequest.email())
                    .orElseThrow(() -> new AppException("Email not found in database.", HttpStatus.NOT_FOUND));
            userCredential.setOngoingLoan(false);
            userCredentialRepo.save(userCredential);

        }


    }

    @Override
    public InstallmentsResponseDto getInstallments(GetInstallmentsDto getInstallmentsDto) {
        List<Installment> installments = installmentRepo.findAllByLoanRequest(UUID.fromString(getInstallmentsDto.loanId()));
        List<Installment> paidInstallment = new ArrayList<>();
        Installment upcomingInstallment = null;
        List<Installment> missedInstallment = new ArrayList<>();
        for (Installment installment : installments) {
            if (installment.getStatus().equals(InstallmentStatus.PAID)) {
                paidInstallment.add(installment);
            } else if ((installment.getScheduledDate()).isBefore(LocalDate.now())) {
                missedInstallment.add(installment);
            } else {
                if(upcomingInstallment == null || installment.getScheduledDate().isBefore(upcomingInstallment.getScheduledDate())){
                    upcomingInstallment = installment;
                }
            }

        }

        return new InstallmentsResponseDto(paidInstallment, upcomingInstallment != null ? List.of(upcomingInstallment): Collections.emptyList(), missedInstallment);
    }


}
