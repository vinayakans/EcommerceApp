package com.ecom.customer.Controller;

import com.ecom.library.library.dto.ProductDto;
import com.ecom.library.library.models.*;
import com.ecom.library.library.service.*;
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
    private OrderService orderService;
    private ProductReviewService productReviewService;
    private CategorySercive categorySercive;

    public HomeController(ProductService productService, CustomerService customerService, OrderService orderService,
                          ProductReviewService productReviewService, CategorySercive categorySercive) {
        this.productService = productService;
        this.customerService = customerService;
        this.orderService = orderService;
        this.productReviewService = productReviewService;
        this.categorySercive = categorySercive;
    }

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

        model.addAttribute("title","Home Page");
        return "index";
    }

    @GetMapping("/product-page/{id}")
    public String storePage(@PathVariable("id")Long id ,
                            Model model ,Principal principal){

         Product product =  productService.findById(id);
        Category itemCategory = product.getCategory();
         boolean user  = false;
         boolean value = false;
         if(principal !=null){
             value = true;
             Customer customer = customerService.findByUsername(principal.getName());
             List<Order> orderList = orderService.customerOrderList(customer.getId());
             for(Order order : orderList){
                 for(OrderDetails orderDetails:order.getOrderDetailsList()){
                     if(id == orderDetails.getProduct().getId()&&order.isOrderDelivered()==true){
                         user = true;
                     }
                 }
             }
             model.addAttribute("user",user);
             model.addAttribute("name",customer.getFirstName());

         }

         model.addAttribute("title","Product View");
         model.addAttribute("value",value);
         double sum =0.0;
         int count =0;
         List<ProductReview> productReviewList = productReviewService.findByProductId(id);
         for(ProductReview productReview:productReviewList){
             sum = sum + productReview.getRating();
             count++;
         }
          double averagereview = sum/count;
        System.out.println(averagereview);
       model.addAttribute("avgreview",averagereview);
         if (productReviewList!=null){
             model.addAttribute("productReviewList",productReviewList);
             model.addAttribute("date",new Date());
         }
         System.out.println(product.getName());
             model.addAttribute("product", product);
             model.addAttribute("itemCategory",itemCategory);
        return "product";
    }

}
