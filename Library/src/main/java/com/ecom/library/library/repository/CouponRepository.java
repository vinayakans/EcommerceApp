package com.ecom.library.library.repository;

import com.ecom.library.library.models.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<Coupon ,Long> {
    Coupon findCouponByCouponCode(String couponCode);

    @Query("select c from Coupon c where c.isDeleted = false")
    List<Coupon> findAll();
}
