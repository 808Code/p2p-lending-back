package com.bonsai.accountservice.repositories;

import com.bonsai.accountservice.models.KYC;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface KYCRepo extends JpaRepository<KYC, UUID> {
}
