package com.ecom.library.library.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    private Date orderDate;
    private Date delivaryDate;
    private Double totalPrice;
    private Double shippingFee;
    private String orderStatus;
    private String paymentMethod;
    private String paymentStatus;
    private boolean orderDelivered;


    private String notes;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id",referencedColumnName = "address_id")
    private Address shippingAddress;

    @ManyToOne(fetch = FetchType.EAGER,cascade = {
            CascadeType.MERGE,CascadeType.PERSIST,
            CascadeType.REFRESH,CascadeType.DETACH})
    @JoinColumn(name = "customer_id",referencedColumnName = "customer_id")
    private Customer customer;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "order")
    private List<OrderDetails> orderDetailsList;

}
