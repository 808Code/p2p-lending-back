package com.bonsai.loanservice.services.impl;

import com.bonsai.accountservice.exceptions.AppException;
import com.bonsai.accountservice.models.UserCredential;
import com.bonsai.accountservice.repositories.UserCredentialRepo;
import com.bonsai.loanservice.dto.LoanRequestDto;
import com.bonsai.loanservice.models.LoanRequest;
import com.bonsai.loanservice.repositories.LoanRequestRepo;
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

    private final LoanRequestRepo loanRequestRepo;

    private final UserCredentialRepo userCredentialRepo;

    @Override
    public LoanRequestDto save(LoanRequestDto loanRequestDto) throws ParseException {

        UserCredential borrower = userCredentialRepo.findById(loanRequestDto.borrowerId()).orElseThrow(
                () -> new AppException("Borrower not found", HttpStatus.BAD_REQUEST)
        );
        LoanRequest loanRequest = LoanRequest.builder()
                .id(loanRequestDto.id())
                .borrower(borrower)
                .requestedDate(LocalDate.now())
                .duration(loanRequestDto.duration())
                .amount(loanRequestDto.amount())
                .loanType(loanRequestDto.loanType())
                .approvalStatus(false)
                .build();
        return new LoanRequestDto(loanRequestRepo.save(loanRequest));
    }

    @Override
    public LoanRequestDto findById(UUID id) {
        LoanRequest loanRequest = loanRequestRepo.findById(id).orElseThrow(() -> new AppException("Loan not found", HttpStatus.BAD_REQUEST));
        return new LoanRequestDto(loanRequest);
    }
}
