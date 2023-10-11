package com.ecom.customer.Controller;

import com.ecom.library.library.dto.ProductDto;
import com.ecom.library.library.models.*;
import com.ecom.library.library.service.CategorySercive;
import com.ecom.library.library.service.CustomerService;
import com.ecom.library.library.service.ProductReviewService;
import com.ecom.library.library.service.ProductService;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import java.security.Principal;
import java.util.Date;
import java.util.List;

@Controller
public class HomeController {
    private ProductService productService;
    private CustomerService customerService;

    public HomeController(ProductService productService, CustomerService customerService,
                          ProductReviewService productReviewService, CategorySercive categorySercive) {
        this.productService = productService;
        this.customerService = customerService;
        this.productReviewService = productReviewService;
        this.categorySercive = categorySercive;
    }

    private ProductReviewService productReviewService;
    private CategorySercive categorySercive;


    @RequestMapping(value = {"/index","/"},method = RequestMethod.GET)
    public String homeController(Model model, Principal principal, HttpSession session) {
//        List<Category> categories = categorySercive.findAllByActivated();
//        model.addAttribute("categories", categories);
        boolean value = false;
        if(principal!=null){
            value = true;
            model.addAttribute("value",value);
           Customer customer = customerService.findByUsername(principal.getName());
            ShoppingCart shoppingCartItem = customer.getShoppingCart();
//            System.out.println(shoppingCartItem.getTotalPrice());
                model.addAttribute("shoppingCart",shoppingCartItem);
                if (shoppingCartItem!=null){
                    session.setAttribute("totalItem",shoppingCartItem.getTotalItems());
                }

            model.addAttribute("name",customer.getFirstName());
        }else {
            model.addAttribute("value",value);
        }
        List<Product> products = productService.findAllByActive();
//        System.out.println(products.get(0).getName());
        if (products!=null){
            model.addAttribute("products",products);
        }


        return "index";
    }

    @GetMapping("/product-page/{id}")
    public String storePage(@PathVariable("id")Long id ,
                            Model model ,Principal principal){

         Product product =  productService.findById(id);
        Category itemCategory = product.getCategory();
         boolean value = false;
         if(principal !=null){
             value = true;
             Customer customer = customerService.findByUsername(principal.getName());
             model.addAttribute("name",customer.getFirstName());
         }

         model.addAttribute("title","Product View");
         model.addAttribute("value",value);

         List<ProductReview> productReviewList = productReviewService.findByProductId(id);
         if (productReviewList!=null){
             model.addAttribute("productReviewList",productReviewList);
             model.addAttribute("date",new Date());
             System.out.println("notnull");
         }
         System.out.println(product.getName());
             model.addAttribute("product", product);
             model.addAttribute("itemCategory",itemCategory);
        return "product";
    }

}
