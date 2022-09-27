package com.bonsai.loanservice.repositories;

import com.bonsai.loanservice.models.Lending;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface LendingRepo extends JpaRepository<Lending, UUID> {

    @Modifying
    @Query(nativeQuery = true, value = """
            insert into lending (id, lent_date, lender_id, loan_request_id, transaction_id)
            values (?1, ?2, ?3, ?4, ?5);
            """)
    void createLending(UUID id, LocalDateTime lentDate, UUID lenderId, UUID loanRequestId, UUID transactionId);
    List<Lending> findAllByLender_Email(String email);
}
