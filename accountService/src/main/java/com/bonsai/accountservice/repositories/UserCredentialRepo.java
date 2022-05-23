package com.bonsai.accountservice.repositories;

import com.bonsai.accountservice.models.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserCredentialRepo extends JpaRepository<UserCredential, UUID> {


}
