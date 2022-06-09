package com.bonsai.accountservice.models;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCredential {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;


    @Column(nullable = false)
    private boolean kycVerified=false;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "kyc_id", referencedColumnName = "id")
    private KYC kyc;
}
