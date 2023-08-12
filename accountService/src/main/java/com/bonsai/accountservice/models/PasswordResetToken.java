package com.bonsai.accountservice.models;

import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_credential_id", nullable = false)
    private UserCredential userCredential;

    private LocalDateTime expiryDate;

    private boolean used = false;

    private LocalDateTime usedDate;

    public PasswordResetToken() {
        this.token = UUID.randomUUID().toString();
        this.expiryDate = LocalDateTime.now().plusHours(6);
    }
}