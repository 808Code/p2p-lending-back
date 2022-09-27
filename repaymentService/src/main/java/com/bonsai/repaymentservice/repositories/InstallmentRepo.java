package com.bonsai.repaymentservice.repositories;

import com.bonsai.repaymentservice.models.Installment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InstallmentRepo extends JpaRepository<Installment, UUID> {
    List<Installment> findByLoanRequest(UUID id);
}
