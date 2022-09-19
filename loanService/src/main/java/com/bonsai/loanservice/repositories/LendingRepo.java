package com.bonsai.loanservice.repositories;

import com.bonsai.loanservice.models.Lending;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LendingRepo extends JpaRepository<Lending, UUID> {
}
