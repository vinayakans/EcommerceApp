package com.ecom.customer.Controller;

import com.ecom.library.library.dto.AddressDto;
import com.ecom.library.library.models.Address;
import com.ecom.library.library.models.Customer;
import com.ecom.library.library.models.Order;
import com.ecom.library.library.service.AddressService;
import com.ecom.library.library.service.CustomerService;
import com.ecom.library.library.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class userDashboardController {
    private CustomerService customerService;
    private OrderService orderService;
    private AddressService addressService;

    public userDashboardController(CustomerService customerService, OrderService orderService, AddressService addressService) {
        this.customerService = customerService;
        this.orderService = orderService;
        this.addressService = addressService;
    }

    @GetMapping("/userDashboard")
    public String userDashboard(Principal principal, Model model){
        if (principal == null){
            return "redirect:/login";
        }
       Customer customer =  customerService.findByUsername(principal.getName());
        if (customer.is_blocked()==true){
            return "redirect:/login";
        }
        String name = customer.getFirstName();
        List<Address> addressList = addressService.findAllById(customer.getId());

        List<Order> customerOrderList = orderService.customerOrderList(customer.getId());
        model.addAttribute("name",name);
        model.addAttribute("customerOrderList",customerOrderList);
        model.addAttribute("addressList",addressList);
        return "userDashboard";
    }
    @GetMapping("/cancle-item/{id}")
    public String cancleItem(@PathVariable("id") Long id){
        orderService.CancleProduct(id);
        return "redirect:/userDashboard";
    }
    @GetMapping("/return-item/{id}")
    public String returnItem(@PathVariable("id") Long id){
        orderService.returnProduct(id);
        return "redirect:/userDashboard";
    }
    @GetMapping("/edit-address/{id}")
    public String editAddress(@PathVariable("id")Long id,Principal principal,Model model){
        if (principal==null){
            return "redirect:/login";
        }
        Address address = addressService.findById(id);
        model.addAttribute("addressDto",address);
        return "editAddresses";
    }
    @PostMapping("/editAddress/{id}")
    public String editAddress(@ModelAttribute("addressDto") Address address,
                              RedirectAttributes redirectAttributes,
                              @PathVariable("id") Long id){
        try{
            addressService.updateAddress(address,id);
            redirectAttributes.addFlashAttribute("success", "Update successfully!");
        }
        catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "update failed");
        }

    return "redirect:/userDashboard";
    }
    @GetMapping("/addAddressFromMange")
    public String addNewAddress(Model model,Principal principal){
        model.addAttribute("addressDto",new AddressDto());

        return "addNewAddress";
    }

}
