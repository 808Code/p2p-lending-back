package com.bonsai.repaymentservice.repositories;

import com.bonsai.repaymentservice.models.PlatformEarning;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface PlatformEarningRepo extends JpaRepository<PlatformEarning, UUID> {
}
