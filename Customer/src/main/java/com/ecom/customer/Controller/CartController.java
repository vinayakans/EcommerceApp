package com.ecom.customer.Controller;

import com.ecom.library.library.models.CartItem;
import com.ecom.library.library.models.Customer;
import com.ecom.library.library.models.Product;
import com.ecom.library.library.models.ShoppingCart;
import com.ecom.library.library.service.CustomerService;
import com.ecom.library.library.service.ProductService;
import com.ecom.library.library.service.ShoppingCartServices;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.hibernate.ObjectDeletedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Set;

@Controller
public class CartController {
    private ShoppingCartServices shoppingCartServices;
    private CustomerService customerService;
    private ProductService productService;

    public CartController(ShoppingCartServices shoppingCartServices,
                          CustomerService customerService, ProductService productService) {
        this.shoppingCartServices = shoppingCartServices;
        this.customerService = customerService;
        this.productService = productService;
    }


    @GetMapping("/cart")
    public String cart(Model model, Principal principal, HttpSession session){

        if(principal==null){
            return "/login";

        }
        boolean value = true;
        String username = principal.getName();
        Customer customer  =customerService.findByUsername(username);
        ShoppingCart shoppingCartItem = customer.getShoppingCart();
        String name = customer.getFirstName();
        if (shoppingCartItem!=null){
            session.setAttribute("totalItem",shoppingCartItem.getTotalItems());
        }
        if (customer.is_blocked()==true){
            return "redirect:/login";
        }
        boolean flag = true;
        if (shoppingCartItem == null){
            flag  = false;

        }else {
            model.addAttribute("shoppingCart",shoppingCartItem);
            Set<CartItem> cartItem = shoppingCartItem.getCartItem();
            for (CartItem cart: cartItem){
                System.out.println(cart.getTotalPrice());
            }
        }
        model.addAttribute("check","shopping cart is empty!!!");
        model.addAttribute("flag",flag);
        model.addAttribute("name",name);
        model.addAttribute("value",value);
        model.addAttribute("title","Cart");
        System.out.println();
        return "cart-page";
    }

    @PostMapping("/add-to-cart")
    public String addToCart(@RequestParam("id") Long productId,
                            @RequestParam(value = "quantity",required = false,defaultValue = "1")int quantity,
                            Principal principal, HttpServletRequest request,RedirectAttributes redirectAttributes){
        if (principal == null ){
            return "redirect:/login";
        }
        Product product = productService.getProductById(productId);
        if ((product.getCurrentQuantity()-quantity)<0){
           redirectAttributes.addFlashAttribute ("outOfStock","item out of stock");
           return "redirect:" +request.getHeader("Referer");
        }
        String username = principal.getName();
        Customer customer = customerService.findByUsername(username);
        if (customer.is_blocked()==true){
            return "redirect:/login";
        }
        ShoppingCart cart = shoppingCartServices.addItemToCart(product,quantity,customer);
        return "redirect:" +request.getHeader("Referer");
    }

    @RequestMapping(value = "/update-cart",params = "action=delete", method = RequestMethod.POST)
    public String deleteCartItem(@RequestParam("id")Long productId,
                                 Model model,Principal principal ){
            if(principal == null){
                return "redirect:/login";
            }else {
                String username = principal.getName();

                Customer customer = customerService.findByUsername(username);
                Product product = productService.getProductById(productId);

                ShoppingCart cart = shoppingCartServices.deleteItemFromCart(product,customer);

                model.addAttribute("shoppingCart",cart);
                return "redirect:/cart";
            }
    }

    @RequestMapping(value = "/update-cart",params = "action=update", method = RequestMethod.POST)
    public String updateCartItem(@RequestParam("id") Long productId,
                                 @RequestParam("quantity") int quantity,
                                 Model model,
                                 Principal principal, RedirectAttributes redirectAttributes){
        if (principal == null){
            return "redirect:/login";
        }
        String username = principal.getName();
        Customer customer = customerService.findByUsername(username);
        Product product = productService.getProductById(productId);
        if (product.getCurrentQuantity()-quantity<0){
            redirectAttributes.addFlashAttribute("error","Not Enough Quantity!!!!");
            return "redirect:/cart";
        }
        ShoppingCart cart = shoppingCartServices.updateItemCart(product,quantity,customer);

        model.addAttribute("shoppingCart",cart);

        return "redirect:/cart";
    }
}
