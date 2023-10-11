package com.ecom.library.library.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cart_item")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    private int Quantity;

    private double unitPrice;

    private double totalPrice;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name = "shopping_cart_id",referencedColumnName = "shopping_cart_id")
    private ShoppingCart Cart;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id",referencedColumnName = "product_id")
    private Product product;

    @Override
    public String toString() {
        return "CartItem{" +
                "id=" + id +
                ", Quantity=" + Quantity +
                ", unitPrice=" + unitPrice +
                ", totalPrice=" + totalPrice +
                ", Cart=" + Cart +
                ", product=" + product +
                '}';
    }
}
