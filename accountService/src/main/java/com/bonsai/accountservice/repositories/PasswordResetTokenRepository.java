package com.bonsai.accountservice.repositories;

import java.util.Optional;

import com.bonsai.accountservice.models.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    @Query(value = "SELECT * FROM password_reset_token t WHERE t.token = ?1 and used = false", nativeQuery = true)
    Optional<PasswordResetToken> findByToken(String token);
}