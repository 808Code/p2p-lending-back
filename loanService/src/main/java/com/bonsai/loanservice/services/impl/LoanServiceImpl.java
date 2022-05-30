package com.bonsai.loanservice.services.impl;

import com.bonsai.accountservice.exceptions.AppException;
import com.bonsai.accountservice.models.UserCredential;
import com.bonsai.accountservice.repositories.UserCredentialRepo;
import com.bonsai.loanservice.dto.LoanDto;
import com.bonsai.loanservice.models.Loan;
import com.bonsai.loanservice.repositories.LoanRepo;
import com.bonsai.loanservice.services.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.UUID;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-05-25
 */
@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {

    private final LoanRepo loanRepo;

    private final UserCredentialRepo userCredentialRepo;

    @Override
    public LoanDto save(LoanDto loanDto) throws ParseException {

        UserCredential borrower = userCredentialRepo.findById(loanDto.borrowerId()).orElseThrow(
                () -> new AppException("Borrower not found", HttpStatus.BAD_REQUEST)
        );
        Loan loan = Loan.builder()
                .id(loanDto.id())
                .borrower(borrower)
                .requestedDate(LocalDate.now())
                .duration(loanDto.duration())
                .amount(loanDto.amount())
                .loanType(loanDto.loanType())
                .approvalStatus(false)
                .build();
        return new LoanDto(loanRepo.save(loan));
    }

    @Override
    public LoanDto findById(UUID id) {
        Loan loan = loanRepo.findById(id).orElseThrow(() -> new AppException("Loan not found", HttpStatus.BAD_REQUEST));
        return new LoanDto(loan);
    }
}
