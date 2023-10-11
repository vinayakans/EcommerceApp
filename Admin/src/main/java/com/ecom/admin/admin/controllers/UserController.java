package com.ecom.admin.admin.controllers;

import com.ecom.library.library.dto.CustomerDto;
import com.ecom.library.library.service.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    public UserController(CustomerService customerService) {
        this.customerService = customerService;
    }
    private CustomerService customerService;

    @GetMapping("/list-users")
    public String listUsers(Model model, Principal principal){
        if(principal==null){
            return "login";
        }
        List<CustomerDto> users = new ArrayList<>();
        users = customerService.findAll();
        model.addAttribute("title","Users");
        model.addAttribute("users",users);
        model.addAttribute("size",users.size());

        return "users";
    }
    @GetMapping("/block-users/{id}")
    public String blockUser(@PathVariable("id") Long id){
        try{
            customerService.blockUser(id);
        }catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/list-users";
    }
}
