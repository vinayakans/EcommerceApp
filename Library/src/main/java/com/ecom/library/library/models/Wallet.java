package com.ecom.library.library.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@Entity
@Table(name = "wallets")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wallet_id")
    private Long id;

    private double balance;
    @ToString.Exclude
    @OneToOne
    @JoinColumn(name = "customer_id",referencedColumnName = "customer_id")
    private Customer customer;

    @ToString.Exclude
    @OneToMany(mappedBy = "wallet")
    private List<WalletHistory> wallerhistoryList;
}
