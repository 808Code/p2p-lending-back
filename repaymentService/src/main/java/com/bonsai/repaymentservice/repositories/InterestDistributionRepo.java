package com.bonsai.repaymentservice.repositories;

import com.bonsai.repaymentservice.models.InterestDistribution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InterestDistributionRepo extends JpaRepository<InterestDistribution, UUID> {
}
