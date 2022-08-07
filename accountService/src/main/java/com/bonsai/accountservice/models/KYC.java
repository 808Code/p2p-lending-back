package com.bonsai.accountservice.models;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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

    @Column
    private String gender;



    @Column(nullable = false)
    private String maritalStatus;


    @Column(nullable = false)
    private boolean children;

    @Column(nullable = false)
    private boolean currentlyStudying;

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


