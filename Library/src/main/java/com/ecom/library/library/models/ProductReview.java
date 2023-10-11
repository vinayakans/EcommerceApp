package com.ecom.library.library.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "product_review")
public class ProductReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    private double rating;//1-5

    @Column(columnDefinition = "TEXT")
    private String review;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
