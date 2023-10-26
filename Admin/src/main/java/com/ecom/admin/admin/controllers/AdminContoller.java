package com.ecom.admin.admin.controllers;


import com.ecom.library.library.models.Admin;
import com.ecom.library.library.service.AdminDashboardService;
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
import java.time.LocalDate;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;

@Controller
public class AdminContoller {
    private AdminService adminService;
    private OrderService orderService;
    private AdminDashboardService adminDashboardService;

    public AdminContoller(AdminService adminService, OrderService orderService,
                          AdminDashboardService adminDashboardService) {
        this.adminService = adminService;
        this.orderService = orderService;
        this.adminDashboardService = adminDashboardService;
    }

    @RequestMapping(value = {"/index","/"},method = RequestMethod.GET)
    public String homePage(Model model, Principal principal){
        if(principal!=null){
            Admin admin = adminService.findByUsername(principal.getName());
            model.addAttribute("adminName",admin.getFirstname());
        }
        Double monthlyIncome = adminDashboardService.totalMonthlyIncome();
        Double DailyIncome = adminDashboardService.DailyIncome();


        model.addAttribute("monthlyIncome",monthlyIncome);
        model.addAttribute("dailyIncome",DailyIncome);

        int orderCount = orderService.totalOrders();
        int pendingCount = orderService.totalPending();


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
