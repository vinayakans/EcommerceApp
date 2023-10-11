package com.ecom.library.library.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="address_id")
    private Long id;
    @Column(name="addLine1")
    private String addressLine1;
    @Column(name="addLine2")
    private String addressLine2;

    private String city;

    private String pinCode;

    private String state;

    private String phoneNumber;

    private String country;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id",referencedColumnName = "customer_id")
    private Customer customer;

    private boolean isDeleted;
}
