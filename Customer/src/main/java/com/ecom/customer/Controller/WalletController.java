package com.ecom.customer.Controller;

import com.ecom.library.library.dto.WalletHistoryDto;
import com.ecom.library.library.models.Customer;
import com.ecom.library.library.models.Wallet;
import com.ecom.library.library.models.WalletHistory;
import com.ecom.library.library.service.CustomerService;
import com.ecom.library.library.service.WalletService;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
public class WalletController {
    private WalletService walletService;

    public WalletController(WalletService walletService, CustomerService customerService) {
        this.walletService = walletService;
        this.customerService = customerService;
    }

    private CustomerService customerService;




    @GetMapping("/addCash")
    public String getWalletPage(Principal principal, Model model){
        if (principal == null){
            return "redirect:/login";
        }
        Customer customer = customerService.findByUsername(principal.getName());
        Wallet wallet = walletService.findByCustomer(customer);
        List<WalletHistoryDto> walletHistoryDtoList =walletService.findAllById(wallet.getId());
        model.addAttribute("wallet",wallet);
        model.addAttribute("walletHistoryList",walletHistoryDtoList);

        return "walletPage";
    }
    @PostMapping("/add-wallet")
    @ResponseBody
    public String addToWallet(@RequestBody Map<String,Object> data, Principal principal, HttpSession session,Model model) throws RazorpayException {

        if (principal == null){
            return "redirect:/login";
        }
        Customer customer = customerService.findByUsername(principal.getName());
        double amount = Double.parseDouble(data.get("amount").toString());
        WalletHistory walletHistory = walletService.save(amount,customer);
        String walletHistoryId = walletHistory.getId().toString();
        session.setAttribute("walletHistoryId",walletHistory.getId());
        model.addAttribute("success","Money Added Successfully");
        RazorpayClient razorpayClient = new RazorpayClient("rzp_test_QMWde1wPMCUdjm",
                "Lk3eaGmBH0rPzpzgYI18tT97");
        JSONObject options = new JSONObject();
        options.put("amount",amount*100);
        options.put("currency","INR");
        options.put("receipt",walletHistoryId);
        com.razorpay.Order orderRazorpay = razorpayClient.orders.create(options);
        return orderRazorpay.toString();

    }
    @PostMapping("/verify-wallet")
    @ResponseBody
    public String verifyWalletPayment(@RequestBody Map<String,Object> data,HttpSession session,Principal principal) throws RazorpayException {
        String secret = "Lk3eaGmBH0rPzpzgYI18tT97";
        String order_id = data.get("razorpay_order_id").toString();
        String payment_id = data.get("razorpay_payment_id").toString();
        String signature = data.get("razorpay_signature").toString();

        JSONObject options = new JSONObject();
        options.put("razorpay_order_id", order_id);
        options.put("razorpay_payment_id", payment_id);
        options.put("razorpay_signature", signature);

        boolean status = Utils.verifyPaymentSignature(options,secret);

        WalletHistory walletHistory = walletService.findById((Long)session.getAttribute("walletHistoryId"));

        Customer customer = customerService.findByUsername(principal.getName());

        walletService.updateWallet(walletHistory,customer,status);
        JSONObject response = new JSONObject();
        response.put("status",status);
        session.removeAttribute("walletHistoryId");
        return response.toString();
    }
}
