package com.bonsai.accountservice.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class  Contact{
    @Id
    @GeneratedValue
    private UUID id;


    String otherEmail;

    String telephone;

    @Column(nullable = false)
    String primaryMobile;

    String secondaryMobile;


    @OneToOne(mappedBy = "contact")
    private KYC kyc;

}
