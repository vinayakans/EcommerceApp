package com.ecom.customer.Controller;

import com.ecom.library.library.dto.AddressDto;
import com.ecom.library.library.models.Customer;
import com.ecom.library.library.models.ShoppingCart;
import com.ecom.library.library.service.AddressService;
import com.ecom.library.library.service.CustomerService;
import com.ecom.library.library.service.ShoppingCartServices;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.security.Principal;


@Controller
public class CustomerController {
    private AddressService addressService;

    private CustomerService customerService;

    public CustomerController(AddressService addressService, CustomerService customerService,
                              ShoppingCartServices shoppingCartServices) {
        this.addressService = addressService;
        this.customerService = customerService;
        this.shoppingCartServices = shoppingCartServices;
    }

    private ShoppingCartServices shoppingCartServices;



    @PostMapping("/addNewAddress")
    public String addNewAddress(@Valid @ModelAttribute("addressDto") AddressDto addressDto,
                                BindingResult result,
                                Principal principal,
                                Model model, RedirectAttributes redirectAttributes, HttpServletRequest request){
        if (principal == null){
            return "redirect:/login";
        }
        Customer customer = customerService.findByUsername(principal.getName());

        if (customer.is_blocked()==true){
            return "redirect:/login";
        }
        try{
            if (result.hasErrors()){
                System.out.println("err_address");
                result.toString();
               redirectAttributes.addFlashAttribute("addressDto",addressDto);
                 return "redirect:" +request.getHeader("Referer");
            }
            addressService.AddAddress(addressDto,customer);
        }catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error","Cannot process at this moment");
        }

        return "redirect:" +request.getHeader("Referer");
    }
    @GetMapping("/delete-address/{id}")
    public String deleteAddress(@PathVariable("id") Long id,Principal principal){
        if (principal == null){
            return "redirect:/login";
        }
        addressService.deleteById(id);
        return "redirect:/userDashboard";
    }

}
