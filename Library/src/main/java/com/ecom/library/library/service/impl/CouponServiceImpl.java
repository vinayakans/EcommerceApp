package com.ecom.library.library.service.impl;

import com.ecom.library.library.dto.CouponDto;
import com.ecom.library.library.models.Coupon;
import com.ecom.library.library.repository.CouponRepository;
import com.ecom.library.library.service.CouponService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CouponServiceImpl implements CouponService {

    private CouponRepository couponRepository;

    public CouponServiceImpl(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @Override
    public void sava(CouponDto couponDto) {

        try{
            Coupon coupon = couponTransfer(couponDto);
            couponRepository.save(coupon);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void updateCoupon(CouponDto couponDto, Long id) {
        try{
            Coupon coupon  = couponRepository.getReferenceById(id);
            coupon.setCouponCode(couponDto.getCouponCode());
            coupon.setDescription(couponDto.getDescription());
            coupon.setCount(couponDto.getCount());
            coupon.setExpDate(couponDto.getExpDate());
            coupon.setPercentage(couponDto.getPercentage());
            coupon.setMaxOff(couponDto.getMaxOff());
            coupon.setMinAmount(couponDto.getMinAmount());
            couponRepository.save(coupon);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public List<CouponDto> findAll() {
        List<Coupon> couponList = couponRepository.findAll();
        List<CouponDto> couponDtoList = couponListTransfer(couponList);
        return couponDtoList;
    }

    @Override
    public CouponDto findById(Long id) {
       Coupon coupon = couponRepository.getReferenceById(id);
       CouponDto couponDto  = couponTransferDto(coupon);
        return couponDto;
    }

    @Override
    public void deleteCoupon(Long id) {
        try{
        couponRepository.deleteById(id);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean enableDisable(Long id) {
        Coupon coupon = couponRepository.getReferenceById(id);
        if (coupon.isEnabled() == false){
            coupon.setEnabled(true);
            couponRepository.save(coupon);
            return true;
        }else {
            coupon.setEnabled(false);
            couponRepository.save(coupon);
            return false;
        }
    }

    public Coupon couponTransfer(CouponDto couponDto){
        Coupon coupon = new Coupon();
        coupon.setCouponCode(couponDto.getCouponCode());
        coupon.setDescription(couponDto.getDescription());
        coupon.setCount(couponDto.getCount());
        coupon.setExpDate(couponDto.getExpDate());
        coupon.setPercentage(couponDto.getPercentage());
        coupon.setMaxOff(couponDto.getMaxOff());
        coupon.setMinAmount(couponDto.getMinAmount());
        return coupon;
    }

    public List<CouponDto> couponListTransfer(List<Coupon> couponList){
        List<CouponDto> couponDtoList = new ArrayList<>();
        for (Coupon list : couponList){
        CouponDto couponDto = new CouponDto();
            couponDto.setId(list.getId());
            couponDto.setCouponCode(list.getCouponCode());
            couponDto.setDescription(list.getDescription());
            couponDto.setCount(list.getCount());
            couponDto.setExpDate(list.getExpDate());
            couponDto.setPercentage(list.getPercentage());
            couponDto.setMaxOff(list.getMaxOff());
            couponDto.setMinAmount(list.getMinAmount());
            couponDto.setEnabled(list.isEnabled());
            couponDtoList.add(couponDto);
        }
        return couponDtoList;
    }
    public CouponDto couponTransferDto(Coupon coupon){
        CouponDto couponDto = new CouponDto();
        couponDto.setId(coupon.getId());
        couponDto.setCouponCode(coupon.getCouponCode());
        couponDto.setPercentage(coupon.getPercentage());
        couponDto.setCount(coupon.getCount());
        couponDto.setDescription(coupon.getDescription());
        couponDto.setExpDate(coupon.getExpDate());
        couponDto.setMaxOff(coupon.getMaxOff());
        couponDto.setMinAmount(coupon.getMinAmount());
        return couponDto;

    }
}
