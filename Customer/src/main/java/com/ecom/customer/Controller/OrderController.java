package com.ecom.customer.Controller;

import com.ecom.library.library.models.Address;
import com.ecom.library.library.models.Customer;
import com.ecom.library.library.models.Order;
import com.ecom.library.library.models.ShoppingCart;
import com.ecom.library.library.service.AddressService;
import com.ecom.library.library.service.CustomerService;
import com.ecom.library.library.service.OrderService;
import com.ecom.library.library.service.ShoppingCartServices;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.apache.catalina.LifecycleState;
import org.json.JSONObject;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

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
    @ResponseBody
    public String placeOrder(@RequestBody Map<String,Object> jsonData, Principal principal , HttpSession session,Model model) throws Exception{
        if(principal == null){
            return "redirect:/login";
        }

        Long selectedAddressId = Long.parseLong(jsonData.get("addressId").toString());
        String payment = jsonData.get("paymentMethod").toString();
        Address address = addressService.findById(selectedAddressId);
        Customer customer =customerService.findByUsername(principal.getName());
        ShoppingCart cart = customer.getShoppingCart();

        if (customer.is_blocked()==true){
            return "redirect:/login";
        }

        String name = customer.getFirstName();
        model.addAttribute("name",name);
        model.addAttribute("value",principal);
        if(payment.equals("razorpay")){
            Order order = orderService.saveOrder(customer,address,cart,payment);
            session.setAttribute("orderId",order.getId());
            String OrderId = order.getId().toString();
            RazorpayClient razorpayClient = new RazorpayClient("rzp_test_NtvmKraF0rlqhk",
                    "LryHfDVeQ7rx2lCRv9hemvRW");
            JSONObject options = new JSONObject();
            options.put("amount",order.getTotalPrice()*100);
            options.put("currency","INR");
            options.put("receipt",order.getId());
            com.razorpay.Order orderRazorpay = razorpayClient.orders.create(options);
            return orderRazorpay.toString();
        } else {
            orderService.saveOrder(customer,address,cart,payment);
            JSONObject options = new JSONObject();
            options.put("status","cash");
            return options.toString();
        }
    }
    @RequestMapping(value = "/verify-payment",method = RequestMethod.POST)
    @ResponseBody
    public String verifyPayment(@RequestBody Map<String,Object> jsonData,Principal principal,HttpSession session)throws Exception{
        if (principal==null){
            return "redirect:/login";
        }
        String secret = "LryHfDVeQ7rx2lCRv9hemvRW";
        String order_id= jsonData.get("razorpay_order_id").toString();
        String payment_id=jsonData.get("razorpay_payment_id").toString();
        String signature=jsonData.get("razorpay_signature").toString();
        JSONObject options = new JSONObject();
        options.put("razorpay_order_id",order_id);
        options.put("razorpay_payment_id",payment_id);
        options.put("razorpay_signature",signature);
        boolean status = Utils.verifyPaymentSignature(options,secret);
        Order order = orderService.findOrderById((Long)session.getAttribute("orderId"));
        if (status){
            orderService.updatePayment(order,status);
            Customer customer  = customerService.findByUsername(principal.getName());
            ShoppingCart cart = customer.getShoppingCart();
            shoppingCart.clearCart(cart);
        }else {
            orderService.updatePayment(order,status);
        }
        JSONObject response = new JSONObject();
        response.put("status",status);
        return response.toString();
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
