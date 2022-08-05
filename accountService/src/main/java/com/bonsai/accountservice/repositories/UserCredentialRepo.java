package com.bonsai.accountservice.repositories;

import com.bonsai.accountservice.models.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserCredentialRepo extends JpaRepository<UserCredential, UUID> {

    Optional<UserCredential> findByIdAndRole(UUID id, String role);
    Optional<UserCredential> findByEmail(String email);
    Optional<UserCredential> findByEmailAndRole(String email, String role);

    @Query(nativeQuery = true, value = "select *\n" +
            "from user_credential u\n" +
            "where lower(u.role) = 'lender'\n" +
            "  and u.last_active_date >= date(current_date - interval '3 months')")
    List<UserCredential> findAllActiveLenders();
}
