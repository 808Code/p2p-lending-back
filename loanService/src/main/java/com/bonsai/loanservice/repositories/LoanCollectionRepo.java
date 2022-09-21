package com.bonsai.loanservice.repositories;

import com.bonsai.loanservice.models.LoanCollection;
import com.bonsai.loanservice.models.LoanRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface LoanCollectionRepo extends JpaRepository<LoanCollection, UUID> {
    @Query(nativeQuery = true, value = "select coalesce(sum(wt.amount), 0) as \"collectedAmount\"\n" +
            "from loan_collection lc\n" +
            "         inner join wallet_transaction wt on wt.id = lc.transaction_id\n" +
            "where lc.loan_id = ?1")
    Long findCollectedAmountByLoanId(UUID loanId);

    List<LoanCollection> findAllByLoanRequestId(UUID id);

    void deleteAllByLoanRequestId(UUID id);
}
