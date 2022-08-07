package com.bonsai.accountservice.models;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Address{
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    String province;

    @Column(nullable = false)
    String district;

    String municipality;

    @Column(nullable = false)
    String ward;

    @Column(nullable = false)
    String tole;

    @Override
    public String toString() {
        return this.district+","+this.province+","+this.municipality+","+this.ward +","+this.tole;
    }

}
