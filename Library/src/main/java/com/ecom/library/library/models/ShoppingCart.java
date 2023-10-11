package com.ecom.library.library.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "shopping_cart")
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shopping_cart_id")
    private Long id;

    private int totalItems;

    private Double totalPrice;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id",referencedColumnName = "customer_id")
    private Customer customer;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "Cart")
    private Set<CartItem> cartItem;

    @Override
    public String toString() {
        return "ShoppingCart{" +
                "id=" + id +
                ", totalItems=" + totalItems +
                ", totalPrice=" + totalPrice +
                ", customer=" + customer +
                '}';
    }
    public ShoppingCart(){
        this.cartItem = new HashSet<>();
        this.totalItems = 0;
        this.totalPrice = 0.0;
    }
}
