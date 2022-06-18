package com.bonsai.accountservice.repositories;

import com.bonsai.accountservice.models.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserCredentialRepo extends JpaRepository<UserCredential, UUID> {

    Optional<UserCredential> findByIdAndRole(UUID id, String role);
    Optional<UserCredential> findByEmail(String email);
    Optional<UserCredential> findByEmailAndRole(String email, String role);
}
