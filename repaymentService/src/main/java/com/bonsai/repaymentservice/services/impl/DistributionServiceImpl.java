package com.bonsai.repaymentservice.services.impl;

import com.bonsai.repaymentservice.constants.InterestRate;
import com.bonsai.repaymentservice.dto.InterestAndEmi;
import com.bonsai.repaymentservice.dto.Lending;
import com.bonsai.repaymentservice.models.InterestDistribution;
import com.bonsai.repaymentservice.repositories.InterestDistributionRepo;
import com.bonsai.repaymentservice.services.DistributionService;
import com.bonsai.repaymentservice.services.EmiService;
import com.bonsai.walletservice.dtos.LoadWalletDto;
import com.bonsai.walletservice.services.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class DistributionServiceImpl implements DistributionService {
    private final EmiService emiService;
    private final WalletService walletService;
    private final InterestDistributionRepo interestDistributionRepo;

    @Override
    public BigDecimal distribute(List<Lending> lendings, BigDecimal distributionAmount, int duration) {
        BigDecimal totalAmount = distributionAmount;
        for (Lending lending : lendings) {
            InterestAndEmi interestAndEmi = emiService.calculateMonthlyInterestAndEMI(lending.getAmount(), duration, InterestRate.LENDER_INTEREST);
            BigDecimal lenderEmi = interestAndEmi.emi();
            totalAmount = totalAmount.subtract(lenderEmi);
            LoadWalletDto loadWalletDto = walletService.loadWallet(
                    lenderEmi,
                    lending.getEmail(),
                    "Rs. "
                            + lenderEmi.setScale(2, RoundingMode.DOWN)
                            + " installment received."
            );
            InterestDistribution interestDistribution = InterestDistribution
                    .builder()
                    .walletTransaction(loadWalletDto.walletTransaction())
                    .lendingId(UUID.fromString(lending.getId()))
                    .build();
            interestDistributionRepo.save(interestDistribution);
        }
        return totalAmount;
    }
}
