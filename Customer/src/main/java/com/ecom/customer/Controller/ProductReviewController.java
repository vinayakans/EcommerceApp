package com.ecom.customer.Controller;

import com.ecom.library.library.models.Customer;
import com.ecom.library.library.models.Product;
import com.ecom.library.library.service.CustomerService;
import com.ecom.library.library.service.ProductReviewService;
import com.ecom.library.library.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class ProductReviewController {
    private ProductReviewService productReviewService;
    private CustomerService customerService;
    private ProductService productService;

    public ProductReviewController(ProductReviewService productReviewService,
                                   CustomerService customerService, ProductService productService) {
        this.productReviewService = productReviewService;
        this.customerService = customerService;
        this.productService = productService;
    }

    @PostMapping("/add-review")
    public String addReview(@RequestParam("product_id")Long product_id,
                            @RequestParam("rating") int rating,
                            @RequestParam("textAreaExample")String reviewText, Principal principal, Model model,
                            RedirectAttributes redirectAttributes){
        if(principal == null){
            return "redirect:/login";
        }
        System.out.println("Product ID: " + product_id);
        System.out.println("Rating: " + rating);
        System.out.println("Review Text: " + reviewText);
        Customer customer = customerService.findByUsername(principal.getName());
        if (customer.is_blocked()==true){
            return "redirect:/login";
        }
        Product product = productService.findById(product_id);
        try{
            productReviewService.saveReview(product,customer,reviewText,rating);
            redirectAttributes.addFlashAttribute("success","Your review added");
        }catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error","your review cannot be added");
        }
        return "redirect:/product-page/"+product_id;
    }



}
