package com.ecom.customer.Controller;

import com.ecom.library.library.models.Coupon;
import com.ecom.library.library.models.ShoppingCart;
import com.ecom.library.library.service.CouponService;
import com.ecom.library.library.service.CustomerService;
import com.ecom.library.library.service.ShoppingCartServices;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class CouponsController {
    private CouponService couponService;
    private CustomerService customerService;
    private ShoppingCartServices shoppingCartServices;

    public CouponsController(CouponService couponService, CustomerService customerService, ShoppingCartServices shoppingCartServices) {
        this.couponService = couponService;
        this.customerService = customerService;
        this.shoppingCartServices = shoppingCartServices;
    }

    @RequestMapping(value = "applyCoupon",method = RequestMethod.POST, params = "action=apply")
    public String applyCoupon(@RequestParam("couponCode")String couponCode, Principal principal,
                              RedirectAttributes redirectAttributes, HttpSession session){
        if (couponService.findByCouponCode(couponCode.toUpperCase())){
            Coupon coupon = couponService.findByCoupon(couponCode.toUpperCase());
            ShoppingCart cart = customerService.findByUsername(principal.getName()).getShoppingCart();
            Double price = cart.getTotalPrice();

            if (coupon.getMinAmount()>price){
                String message = "Minimum amount for"+coupon.getCouponCode()+"is"+coupon.getMinAmount();
                redirectAttributes.addFlashAttribute("minAmount",message);
                return "redirect:/checkout";
            }
            session.setAttribute("Price",price);

            Double newTotalPrice = couponService.applyCoupon(couponCode.toUpperCase(),price);

            shoppingCartServices.updateShoppingCartPrice(newTotalPrice, principal.getName());

            redirectAttributes.addFlashAttribute("success", "Coupon applied Successfully");
            redirectAttributes.addAttribute("couponCode", couponCode);
            redirectAttributes.addAttribute("couponOff", coupon.getPercentage());

        }else {
            redirectAttributes.addFlashAttribute("error","Coupon is Invalid!!!");
        }

        return "redirect:/checkout";
    }
    @RequestMapping(value = "applyCoupon",method = RequestMethod.POST,params = "action=remove")
    public String removeCoupon(Principal principal,RedirectAttributes redirectAttributes,
                               HttpSession session){

        Double price = (Double) session.getAttribute("Price");
        shoppingCartServices.updateShoppingCartPrice( price,principal.getName());
        redirectAttributes.addFlashAttribute("success","coupon Removed");
         return "redirect:/checkout";

    }
}

