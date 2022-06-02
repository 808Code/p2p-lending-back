package com.bonsai.accountservice.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Builder
@Data
@Entity
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

    @Column(nullable = false)
    String wardNo;

    @Column(nullable = false)
    String tole;

    @Override
    public String toString() {
        return this.province+","+this.district+","+this.wardNo+","+this.tole;
    }

}
