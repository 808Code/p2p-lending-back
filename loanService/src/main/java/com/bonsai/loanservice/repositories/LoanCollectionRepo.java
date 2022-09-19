package com.bonsai.loanservice.repositories;

import com.bonsai.loanservice.models.LoanCollection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LoanCollectionRepo extends JpaRepository<LoanCollection, UUID> {
    Optional<LoanCollection> findByLoanRequest_Id(UUID loanId);
}
