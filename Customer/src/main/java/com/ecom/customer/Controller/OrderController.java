package com.ecom.customer.Controller;

import com.ecom.library.library.models.*;
import com.ecom.library.library.service.*;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@Controller
public class OrderController {
    private OrderService orderService;
    private CustomerService customerService;

    public OrderController(OrderService orderService, CustomerService customerService,
                           ShoppingCartServices shoppingCart, WalletService walletService,
                           AddressService addressService) {
        this.orderService = orderService;
        this.customerService = customerService;
        this.shoppingCart = shoppingCart;
        this.walletService = walletService;
        this.addressService = addressService;
    }

    private ShoppingCartServices shoppingCart;
    private WalletService walletService;



    private AddressService addressService;

    @PostMapping("/placeOrder")
    @ResponseBody
    public String placeOrder(@RequestBody Map<String,Object> jsonData, Principal principal ,
                             HttpSession session,Model model) throws Exception{
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
            System.out.println(order.getId());
            session.setAttribute("orderId",order.getId());
            String OrderId = order.getId().toString();
            RazorpayClient razorpayClient = new RazorpayClient("rzp_test_QMWde1wPMCUdjm",
                    "Lk3eaGmBH0rPzpzgYI18tT97");
            JSONObject options = new JSONObject();
            options.put("amount",order.getTotalPrice()*100);
            options.put("currency","INR");
            options.put("receipt",order.getId().toString());
            com.razorpay.Order orderRazorpay = razorpayClient.orders.create(options);
            System.out.println(orderRazorpay);
            return orderRazorpay.toString();

        } else if (payment.equals("wallet")) {
            WalletHistory walletHistory = walletService.debit(customer.getWallet(),cart.getTotalPrice());
            Order order = orderService.saveOrder(customer,address,cart,payment);
            walletService.saveOrderId(order,walletHistory);

            JSONObject options = new JSONObject();
            options.put("status","wallet");
            return options.toString();
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
        String secret = "Lk3eaGmBH0rPzpzgYI18tT97";
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
            orderService. updatePayment(order,status);
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
    @GetMapping("/order-confirmation")
    public String OrderConfirm(Model model,Principal principal,HttpSession session){
        boolean value = false;
        if (principal!=null){
            value = true;
        }
        Order order = orderService.findOrderById((Long)session.getAttribute("orderId"));
        String name = customerService.findByUsername(principal.getName()).getFirstName();
        model.addAttribute("order",order);
        model.addAttribute("value",value);
        model.addAttribute("name",name);
        model.addAttribute("success","success");
        return "order-confirmation";
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
