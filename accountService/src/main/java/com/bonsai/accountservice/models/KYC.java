package com.bonsai.accountservice.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class KYC {

    @Id
    @GeneratedValue
    private UUID id;


    @Column(nullable = false)
    private String firstName;

    @Column
    private String middleName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String citizenShipNumber;


    @Column(nullable = false)
    private LocalDate dob;



    @Column(nullable = false)
    private String maritalStatus;


    @Column(nullable = false)
    private boolean children;

    @Column(nullable = false)
    private boolean currentlyStudying;

    @Column(nullable = false)
    private String occupation;

    @Column(nullable = false)
    private String temporaryAddress;


    @Column(nullable = false)
    private String permanentAddress;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "contact_id", referencedColumnName = "id")
    private Contact contact;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "finance_id", referencedColumnName = "id")
    private Finance finance;

    @Column
    private String profilePhoto;

    @Column
    private String  citizenShipPhotoFront;

    @Column
    private String  citizenShipPhotoBack;



}


