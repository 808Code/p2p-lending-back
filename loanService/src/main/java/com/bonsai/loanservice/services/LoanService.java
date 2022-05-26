package com.bonsai.loanservice.services;

import com.bonsai.loanservice.dto.LoanDto;

import java.text.ParseException;
import java.util.List;
import java.util.UUID;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-05-25
 */
public interface LoanService {
    LoanDto save(LoanDto loanDto) throws ParseException;
    LoanDto findById(UUID id);
}
