package com.bonsai.loanservice.repositories;

import com.bonsai.loanservice.models.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-05-25
 */
public interface LoanRepo extends JpaRepository<Loan, UUID> {
}
