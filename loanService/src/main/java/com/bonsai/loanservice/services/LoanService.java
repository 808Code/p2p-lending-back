package com.bonsai.loanservice.services;

import com.bonsai.loanservice.dto.LoanRequestDto;

import java.text.ParseException;
import java.util.UUID;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-05-25
 */
public interface LoanService {
    LoanRequestDto save(LoanRequestDto loanRequestDto) throws ParseException;
    LoanRequestDto findById(UUID id);
}
