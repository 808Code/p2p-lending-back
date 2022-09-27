package com.bonsai.loanservice.services.impl;

import com.bonsai.accountservice.constants.Roles;
import com.bonsai.accountservice.models.UserCredential;
import com.bonsai.accountservice.repositories.UserCredentialRepo;
import com.bonsai.loanservice.constants.LoanRequestAmount;
import com.bonsai.loanservice.constants.LoanStatus;
import com.bonsai.loanservice.constants.LoanType;
import com.bonsai.loanservice.dto.LoanRequestDto;
import com.bonsai.loanservice.dto.LoanResponse;
import com.bonsai.loanservice.models.LoanRequest;
import com.bonsai.loanservice.repositories.LoanCollectionRepo;
import com.bonsai.loanservice.repositories.LoanRequestRepo;
import com.bonsai.loanservice.services.LoanService;
import com.bonsai.sharedservice.exceptions.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
    @Transactional
    public LoanResponse save(LoanRequestDto loanRequestDto) {

        UserCredential borrower = userCredentialRepo.findByEmailAndRole(loanRequestDto.borrower(), Roles.BORROWER).orElseThrow(
                () -> new AppException("Borrower not found", HttpStatus.BAD_REQUEST)
        );

        if (!borrower.isKycVerified()) {
            throw new AppException("Kyc not verified", HttpStatus.BAD_REQUEST);
        }

        if (borrower.isOngoingLoan()) {
            throw new AppException("Borrower already has an ongoing loan", HttpStatus.BAD_REQUEST);
        }

        if (loanRequestDto.amount() < LoanRequestAmount.MIN || loanRequestDto.amount() > LoanRequestAmount.MAX) {
            throw new AppException("Loan amount must be in between Rs." + LoanRequestAmount.MIN + " and Rs." + LoanRequestAmount.MAX, HttpStatus.BAD_REQUEST);
        }

        if (loanRequestDto.amount() % LoanRequestAmount.MIN != 0) {
            throw new AppException("Loan amount must be in the multiples of Rs." + LoanRequestAmount.MIN, HttpStatus.BAD_REQUEST);
        }

        LoanRequest loanRequest = LoanRequest.builder()
                .borrower(borrower)
                .requestedDate(LocalDate.now())
                .duration(loanRequestDto.duration())
                .amount(loanRequestDto.amount())
                .remainingAmount(loanRequestDto.amount())
                .loanType(loanRequestDto.loanType())
                .loanStatus(LoanStatus.NEW)
                .build();

        borrower.setOngoingLoan(true);
        userCredentialRepo.save(borrower);

        loanRequest = loanRequestRepo.saveAndFlush(loanRequest);

        return new LoanResponse(loanRequest);
    }

    @Override
    public LoanRequestDto findById(UUID id) {
        LoanRequest loanRequest = loanRequestRepo.findById(id).orElseThrow(() -> new AppException("Loan not found", HttpStatus.BAD_REQUEST));
        return new LoanRequestDto(loanRequest);
    }

    @Override
    public List<LoanResponse> findAllByBorrower(String borrowerEmail) {
        UserCredential borrower = userCredentialRepo.findByEmailAndRole(borrowerEmail, Roles.BORROWER).orElseThrow(
                () -> new AppException("Borrower not found", HttpStatus.BAD_REQUEST)
        );

        return LoanResponse.loanToDtoList(loanRequestRepo.findAllByBorrower(borrower.getEmail()));
    }

    @Override
    public List<String> findAllLoanTypes() {
        List<String> loanTypes = new ArrayList<>();
        loanTypes.add(LoanType.EDUCATIONAL);
        loanTypes.add(LoanType.VEHICLE);
        loanTypes.add(LoanType.AGRICULTURAL);
        loanTypes.add(LoanType.HEALTH);
        loanTypes.add(LoanType.HOME);
        loanTypes.add(LoanType.BUSINESS);
        return loanTypes;
    }

    @Override
    @Transactional
    public void deleteLoanSuggestion(UUID loanId, String lenderEmail) {
        try{
            loanRequestRepo.clearLoanSuggestion(loanId, lenderEmail);
        }catch (Exception exception) {
            throw new AppException(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
