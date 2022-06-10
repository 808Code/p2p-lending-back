package com.bonsai.accountservice.repositories;

import com.bonsai.accountservice.models.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ContactRepo extends JpaRepository<Contact, UUID> {
}
