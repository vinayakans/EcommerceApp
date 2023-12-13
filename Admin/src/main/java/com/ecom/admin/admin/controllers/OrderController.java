package com.ecom.admin.admin.controllers;

import com.ecom.library.library.models.Customer;
import com.ecom.library.library.models.Order;
import com.ecom.library.library.models.OrderDetails;
import com.ecom.library.library.service.CustomerService;
import com.ecom.library.library.service.OrderDetailService;
import com.ecom.library.library.service.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class OrderController {
    private OrderService orderService;
    private CustomerService customerService;
    private OrderDetailService orderDetailService;

    public OrderController(OrderService orderService, CustomerService customerService,
                           OrderDetailService orderDetailService) {
        this.orderService = orderService;
        this.customerService = customerService;
        this.orderDetailService = orderDetailService;
    }

    @ModelAttribute("selectedStatus")
    public StatusForm getStatusForm()  {
        return new StatusForm();
    }
    @GetMapping("/list-order")
    public String OrderController(Principal principal, Model model){
        if(principal == null){
            return "redirect:/login";
        }
        List<Order> ordersList = orderService.ListOrders();
//        List<OrderDetails> orderDetailsList =
        model.addAttribute("orderList",ordersList);
        model.addAttribute("size",ordersList.size());
        model.addAttribute("selectedStatus",new StatusForm());
        return "OrderList";
    }
    @GetMapping("/update-status/{id}")
    public String updateStatus(@PathVariable("id") Long id){
            orderService.acceptOrder("CONFIRM",id);
            return "redirect:/list-order";
    }
    @GetMapping("/orders")
    public String getOrders(Principal principal, Model model,
                            @RequestParam(name = "status",required = false,defaultValue = "")String orderStatus,
                            @RequestParam(name = "orderId",required = false,defaultValue = "0")long order_id){

        if(principal==null){
            return "redirect:/login";
        }else{
            System.out.println(orderStatus + order_id );
            orderService.updateOrderStatus(orderStatus,order_id);
            List<Order> ordersList = orderService.ListOrders();
            model.addAttribute("orders",ordersList);

            return "redirect:/list-order";

        }

    }
    @GetMapping("/order-details/{id}")
    public String orderDetails(@PathVariable("id") Long id,Principal principal,Model model){
        if (principal==null){
            return "redirect:/login";
        }
        Order order = orderService.findById(id);
        model.addAttribute("order",order);
        List<OrderDetails> orderDetailsList = orderDetailService.orderDetailsByOrderId(id);
        model.addAttribute("orderDetailList",orderDetailsList);
        return "Order-details";
    }
    @GetMapping("/cancel-item/{id}")
    public String orderCancel(@PathVariable("id")Long id){
        orderService.CancelProduct(id);
        return "redirect:/list-order";
    }

}
