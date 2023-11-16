package com.ecom.library.library.repository;

import com.ecom.library.library.models.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<Coupon ,Long> {
}
