package com.bonsai.loansuggestionservice.wallet.repositories;

import com.bonsai.loansuggestionservice.wallet.models.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-08-06
 */
public interface WalletTransactionRepo extends JpaRepository<WalletTransaction, UUID> {
}
