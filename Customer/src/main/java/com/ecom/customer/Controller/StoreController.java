package com.ecom.customer.Controller;


import com.ecom.library.library.models.Category;
import com.ecom.library.library.models.Customer;
import com.ecom.library.library.models.Product;
import com.ecom.library.library.service.CategorySercive;
import com.ecom.library.library.service.CustomerService;
import com.ecom.library.library.service.ProductService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;

@Controller
public class StoreController {

    private ProductService productService;
    private CategorySercive categorySercive;

    public StoreController(ProductService productService, CategorySercive categorySercive, CustomerService customerService) {
        this.productService = productService;
        this.categorySercive = categorySercive;
        this.customerService = customerService;
    }

    private CustomerService customerService;


    @Transactional
    @GetMapping("/store-page")
    public String storePage(Model model, Principal principal){
        System.out.println("1");
        boolean value = false;
        if(principal!=null){
            value = true;
            Customer customer = customerService.findByUsername(principal.getName());
            String name = customer.getFirstName();
            model.addAttribute("name",name);
        }
        List<Product> products = productService.findAllByActive();
        List<Category> categories = categorySercive.findAllByActivated();
        if (products!=null){
            model.addAttribute("products",products);
            model.addAttribute("categories",categories);
        }
        return "store";
    }
    @Transactional
    @GetMapping("/byCategory/{id}")
    public String byCategoryView(@PathVariable("id") Long id,
                                 Model model,Principal principal){

        List<Product> productList = productService.findByCategory(id);
        List<Category> categories = categorySercive.findAllByActivated();
        model.addAttribute("products",productList);
        model.addAttribute("categories",categories);
        return "store";
    }
}
