package com.bonsai.accountservice.repositories;

import com.bonsai.accountservice.models.Finance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface FinanceRepo extends JpaRepository<Finance, UUID> {
}
