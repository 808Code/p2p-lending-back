package com.bonsai.accountservice.repositories;

import com.bonsai.accountservice.models.KYC;
import com.bonsai.accountservice.models.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserCredentialRepo extends JpaRepository<UserCredential, UUID> {

    Optional<UserCredential> findByIdAndRole(UUID id, String role);

    Optional<UserCredential> findByEmail(String email);

    Optional<UserCredential> findByEmailAndRole(String email, String role);

    @Query(nativeQuery = true, value = """
            select *
            from user_credential u
            where lower(u.role) = 'lender'
              and u.last_active_date >= date(current_date - interval '3 months')""")
    List<UserCredential> findAllActiveLenders();

    @Modifying
    @Query(nativeQuery = true, value = "insert into wallet (id, user_id, amount)\n" +
            "values (?1, ?2, 0)")
    void createWallet(UUID id, UUID userId);


    @Query(nativeQuery = true, value = """
            select *
            from user_credential u
            where u.kyc_verified = false
            """
    )
    List<UserCredential> findAllKycUnverifiedUsers();
    @Modifying
    @Query(nativeQuery = true,value = """
              update kyc
              set kyc_message = ?1
              where id = ?2
            """)
    void saveAdminKycMessage(String message, UUID kycId);

    @Query(nativeQuery = true,value = """
               select kyc_message
               from kyc
               where id = ?1
            """)
    String getAdminKycMessage(UUID email);
}
