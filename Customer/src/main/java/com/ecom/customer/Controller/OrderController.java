package com.ecom.customer.Controller;

import com.ecom.library.library.models.Address;
import com.ecom.library.library.models.Customer;
import com.ecom.library.library.models.Order;
import com.ecom.library.library.models.ShoppingCart;
import com.ecom.library.library.service.AddressService;
import com.ecom.library.library.service.CustomerService;
import com.ecom.library.library.service.OrderService;
import com.ecom.library.library.service.ShoppingCartServices;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class OrderController {
    private OrderService orderService;
    private CustomerService customerService;

    private ShoppingCartServices shoppingCart;

    public OrderController(OrderService orderService, CustomerService customerService, AddressService addressService,ShoppingCartServices shoppingCart) {
        this.orderService = orderService;
        this.customerService = customerService;
        this.addressService = addressService;
        this.shoppingCart = shoppingCart;
    }

    private AddressService addressService;

    @PostMapping("/placeOrder")
    public String placeOrder(@RequestParam("selectedAddressId") Long selectedAddressId,
                             @RequestParam("payment_option")String payment, Principal principal , Model model){
        if(principal == null){
            return "redirect:/login";
        }

        System.out.println(payment);
        Address address = addressService.findById(selectedAddressId);
        Customer customer =customerService.findByUsername(principal.getName());
        if (customer.is_blocked()==true){
            return "redirect:/login";
        }
        ShoppingCart cart = customer.getShoppingCart();
        orderService.saveOrder(customer,address,cart,payment);

        String name = customer.getFirstName();
        model.addAttribute("name",name);
        model.addAttribute("value",principal);

        return "successPage";
    }

    @GetMapping("/orderTracking/{id}")
    public String orderTracking(@PathVariable("id")Long id,Principal principal, Model model){
       boolean value =true;
        model.addAttribute("value",value);
        Customer customer = customerService.findByUsername(principal.getName());
        model.addAttribute("name",customer.getFirstName());
        Order order = orderService.findById(id);
        model.addAttribute("customer",customer);
        model.addAttribute("order",order);
        int statusCode = 1;
        System.out.println(order.getOrderStatus());
        String status1 = order.getOrderStatus();
        if(status1.equals("CONFIRM")){
            statusCode = 2;
        }
        else if(status1.equals("SHIPPED")){
            statusCode =3;
        }
        else if(status1.equals("DELIVERED")){
            statusCode = 4;
        }
        System.out.println(statusCode);
        model.addAttribute("statusCode",statusCode);

        return"orders-tracking";
    }
}
