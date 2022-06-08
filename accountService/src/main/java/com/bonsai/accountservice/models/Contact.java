package com.bonsai.accountservice.models;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Builder
@Getter
@Setter
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

}
