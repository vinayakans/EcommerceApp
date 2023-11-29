package com.ecom.library.library.service;

import com.ecom.library.library.dto.CouponDto;
import com.ecom.library.library.models.Coupon;

import java.util.List;

public interface CouponService {
    void sava(CouponDto couponDto);
    void updateCoupon(CouponDto couponDto,Long id);
    List<CouponDto> findAll();
    CouponDto findById(Long id);
    void deleteCoupon(Long id);
    void softDelete(Long id);
    boolean enableDisable(Long id);
    boolean findByCouponCode(String couponCode);
    Coupon findByCoupon(String coupon);
    Double applyCoupon(String coupon,double price);

}
