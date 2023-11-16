package com.ecom.library.library.models;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.context.annotation.EnableLoadTimeWeaving;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "coupons")
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long id;

    private String couponCode;

    private String description;

    private int percentage;

    private int maxOff;

    private int count;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate expDate;

    private boolean enabled;

    private int minAmount;

    private boolean isExpired(){
        return (this.count == 0||this.expDate.isBefore(LocalDate.now()));
    }
}
