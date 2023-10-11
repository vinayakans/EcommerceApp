package com.ecom.library.library.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "order_detail")
public class OrderDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_id")
    private Long id;

    private int quantity;
    private double totalPrice;
    private double unitPrice;

    @ManyToOne(fetch = FetchType.LAZY,cascade = {
            CascadeType.MERGE,CascadeType.PERSIST,
            CascadeType.REFRESH,CascadeType.DETACH})
    @JoinColumn(name = "order_id",referencedColumnName = "order_id")
    private Order order;

   @ManyToOne(fetch = FetchType.LAZY,cascade = {
           CascadeType.MERGE,CascadeType.PERSIST,
           CascadeType.REFRESH,CascadeType.DETACH})
   @JoinColumn(name = "product_id",referencedColumnName = "product_id")
   private Product product;

}
