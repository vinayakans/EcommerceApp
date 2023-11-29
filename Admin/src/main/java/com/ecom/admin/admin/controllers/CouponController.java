package com.ecom.admin.admin.controllers;

import com.ecom.library.library.dto.CategoryDto;
import com.ecom.library.library.dto.CouponDto;

import com.ecom.library.library.service.CouponService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class CouponController {
    private CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @GetMapping("/coupons")
    public String couponsList(Model model, Principal principal){
        List<CouponDto> couponDtoList = couponService.findAll();
        model.addAttribute("coupons",couponDtoList);
        model.addAttribute("size",couponDtoList.size());

        return "coupons";
    }
    @GetMapping("/add-couponsPage")
    public String addCouponsPage(Model model, Principal principal){

        model.addAttribute("coupon" ,new CouponDto());
        return "add-Coupon";
    }
    @RequestMapping(value = "/add-coupons",method = RequestMethod.POST)
    public String addCoupons(@Valid @ModelAttribute("coupon")CouponDto couponDto, RedirectAttributes redirectAttributes, Principal principal
                                ,BindingResult result){
        if (principal==null){
            return "redirect:/login";
        }
        if (result.hasErrors()){
            redirectAttributes.addFlashAttribute("error","Oops! Something went wrong");
            return "redirect:/add-couponPage";
        }
        else{
            couponService.sava(couponDto);
            redirectAttributes.addFlashAttribute("success","Coupon Added successfully");
        }
    return "redirect:/coupons";
    }
    @GetMapping("/update-coupon/{id}")
    public String updateCouponPage(@PathVariable("id")Long id, Model model,Principal principal){
        if (principal==null){
            return "redirect:/login";
        }
        CouponDto couponDto = couponService.findById(id);
        model.addAttribute("coupon",couponDto);
        return "update-Coupon";
    }
    @PostMapping("/update-coupon/{id}")
    public String updateCoupon(@Valid @ModelAttribute("coupon")CouponDto couponDto ,BindingResult result ,
                               RedirectAttributes redirectAttributes,Principal principal,@PathVariable("id")Long id){
        if (principal==null){
            return "redirect:/login";
        }
        if (result.hasErrors()){
            redirectAttributes.addFlashAttribute("error","Oops!! Something went wrong");
            return "redirect:/update-coupon/"+id;
        }

        couponService.updateCoupon(couponDto,id);
        redirectAttributes.addFlashAttribute("success","Updated Successfully");
        return"redirect:/coupons";
    }
    @GetMapping("/delete-coupon/{id}")
    public String deleteCoupon(@PathVariable("id")Long id, RedirectAttributes redirectAttributes,Principal principal){
        if (principal==null){
            return "redirect:/login";
        }
        couponService.deleteCoupon(id);
        redirectAttributes.addFlashAttribute("error","Item Deleted");
        return "redirect:/coupons";
    }
    @GetMapping("/couponAction/{id}")
    public String enableCoupon(@PathVariable("id")Long id, Principal principal,RedirectAttributes redirectAttributes){
        if (principal==null){
            return "redirect:/login";
        }
         if (couponService.enableDisable(id)){
             redirectAttributes.addFlashAttribute("error","coupon Disabled");
             return "redirect:/coupons";
         }
        else{
             redirectAttributes.addFlashAttribute("success","coupon Enabled");
             return "redirect:/coupons";
         }
    }
    @GetMapping("/softDelete-coupon/{id}")
    public String softDelete(@PathVariable("id")Long id,Model model,RedirectAttributes redirectAttributes,
                             Principal principal){
        if (principal == null){
            return "redirect:/login";
        }
        couponService.softDelete(id);

        return "redirect:/coupons";
    }

}
