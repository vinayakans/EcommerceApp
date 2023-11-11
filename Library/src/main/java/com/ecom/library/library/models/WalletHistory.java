package com.ecom.library.library.models;

import com.ecom.library.library.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "wallet_history")
public class WalletHistory {
    @Id
    @Column(name = "wallet_history_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;

    private TransactionType transactionType;

    private LocalDate transactionDate;
    private String TransactionStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "wallet_id",referencedColumnName = "wallet_id")
    private Wallet wallet;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id",referencedColumnName = "order_id")
    private Order order;
}
