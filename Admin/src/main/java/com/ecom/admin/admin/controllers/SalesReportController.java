package com.ecom.admin.admin.controllers;

import com.ecom.library.library.models.Order;
import com.ecom.library.library.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Calendar;
import java.util.List;

@Controller
public class SalesReportController {
    private OrderService orderService;

    public SalesReportController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/salesreport")
    public String SalesReport(Model model){
        List<Order> orderList = orderService.findAll();
        model.addAttribute("size",orderList.size());
        return "salesreport";
    }
    @GetMapping("/getSalesReport")
    public String getSalesReport(@RequestParam("report") String reportValue,Calendar calendar){
        System.out.println(reportValue);
        return "redirect:/salesreport";
    }

}


