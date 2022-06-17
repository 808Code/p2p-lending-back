package com.bonsai.loanservice.services.impl;

import com.bonsai.accountservice.constants.Roles;
import com.bonsai.accountservice.models.UserCredential;
import com.bonsai.accountservice.repositories.UserCredentialRepo;
import com.bonsai.loanservice.dto.LoanRequestDto;
import com.bonsai.loanservice.models.LoanRequest;
import com.bonsai.loanservice.repositories.LoanRequestRepo;
import com.bonsai.loanservice.services.LoanService;
import com.bonsai.sharedservice.exceptions.AppException;
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

    private final LoanRequestRepo loanRequestRepo;

    private final UserCredentialRepo userCredentialRepo;

    @Override
    public LoanRequestDto save(LoanRequestDto loanRequestDto) {

        UserCredential borrower = userCredentialRepo.findByIdAndRole(loanRequestDto.borrowerId(), Roles.BORROWER).orElseThrow(
                () -> new AppException("Borrower not found", HttpStatus.BAD_REQUEST)
        );

        if (!borrower.isKycVerified()) {
            throw new AppException("Kyc not verified", HttpStatus.BAD_REQUEST);
        }

        if (borrower.isOngoingLoan()) {
            throw new AppException("Borrower already has an ongoing loan", HttpStatus.BAD_REQUEST);
        }

        LoanRequest loanRequest = LoanRequest.builder()
                .id(loanRequestDto.id())
                .borrower(borrower)
                .requestedDate(LocalDate.now())
                .duration(loanRequestDto.duration())
                .amount(loanRequestDto.amount())
                .loanType(loanRequestDto.loanType())
                .approvalStatus(false)
                .build();

        borrower.setOngoingLoan(true);
        userCredentialRepo.save(borrower);
        return new LoanRequestDto(loanRequestRepo.save(loanRequest));
    }

    @Override
    public LoanRequestDto findById(UUID id) {
        LoanRequest loanRequest = loanRequestRepo.findById(id).orElseThrow(() -> new AppException("Loan not found", HttpStatus.BAD_REQUEST));
        return new LoanRequestDto(loanRequest);
    }
}
