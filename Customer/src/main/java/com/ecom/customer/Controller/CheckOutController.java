package com.ecom.customer.Controller;

import com.ecom.library.library.dto.AddressDto;
import com.ecom.library.library.models.*;
import com.ecom.library.library.repository.AddressRepository;
import com.ecom.library.library.service.CustomerService;
import com.ecom.library.library.service.WalletService;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@Controller
public class CheckOutController {
    private CustomerService customerService;
    private AddressRepository addressRepository;
    private WalletService walletService;

    public CheckOutController(CustomerService customerService, AddressRepository addressRepository,
                              WalletService walletService) {
        this.customerService = customerService;
        this.addressRepository = addressRepository;
        this.walletService = walletService;
    }

    @GetMapping("/checkout")
    public String checkOutController(Principal principal, Model model){
        if (principal == null){
            return  "redirect:/login";
        }

        String username = principal.getName();
        System.out.println(username);
        Customer customer  =customerService.findByUsername(username);
        if (customer.is_blocked()==true){
            return "redirect:/login";
        }
        ShoppingCart shoppingCartItem = customer.getShoppingCart();
        System.out.println(shoppingCartItem);
        if(shoppingCartItem == null){
            System.out.println("nullified");
            model.addAttribute("check","the cart is empty");
            return "redirect:/cart";
        }
        else {
            model.addAttribute("shoppingCart",shoppingCartItem);
        }
        List<Address> addressList = addressRepository.findAllById(customer.getId());
        if (addressList == null){
            model.addAttribute("addressNull","NO Shipping Addresses");
        }
        Wallet wallet = walletService.findByCustomer(customer);
        model.addAttribute("wallet",wallet);

        model.addAttribute("addressList",addressList);

        model.addAttribute("addressDto",new AddressDto());


        return "shop-checkout";
    }

}
