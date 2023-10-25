package com.ecom.admin.admin.controllers;


import com.ecom.library.library.models.Admin;
import com.ecom.library.library.service.AdminService;
import com.ecom.library.library.service.OrderService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.time.Month;
import java.util.Calendar;

@Controller
public class AdminContoller {
    private AdminService adminService;
    private OrderService orderService;

    public AdminContoller(AdminService adminService, OrderService orderService) {
        this.adminService = adminService;
        this.orderService = orderService;
    }

    @RequestMapping(value = {"/index","/"},method = RequestMethod.GET)
    public String homePage(Model model, Principal principal){
        if(principal!=null){
            Admin admin = adminService.findByUsername(principal.getName());
            model.addAttribute("adminName",admin.getFirstname());
        }
        int totalMonthlyIncome = orderService.monthlyEarning();
        int totalDailyIncome;


        int orderCount = orderService.totalOrders();
        int pendingCount = orderService.totalPending();

        model.addAttribute("monthlyIncome",totalMonthlyIncome);
        model.addAttribute("totalOrder",orderCount);
        model.addAttribute("totalPending",pendingCount);

        model.addAttribute("title","Home Page");
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication instanceof AnonymousAuthenticationToken){
            return "redirect:/login";
        }
        return "index";

    }

}
