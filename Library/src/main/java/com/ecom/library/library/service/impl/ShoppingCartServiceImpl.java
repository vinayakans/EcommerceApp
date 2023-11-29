package com.ecom.library.library.service.impl;

import com.ecom.library.library.models.CartItem;
import com.ecom.library.library.models.Customer;
import com.ecom.library.library.models.Product;
import com.ecom.library.library.models.ShoppingCart;
import com.ecom.library.library.repository.CartItemRepository;
import com.ecom.library.library.repository.ShoppingCartRepository;
import com.ecom.library.library.service.CustomerService;
import com.ecom.library.library.service.ShoppingCartServices;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartServices {

    private CartItemRepository cartItemRepository;
    private CustomerService customerService;
    private ShoppingCartRepository shoppingCartRepository;

    public ShoppingCartServiceImpl(CartItemRepository cartItemRepository,
                                   CustomerService customerService, ShoppingCartRepository shoppingCartRepository) {
        this.cartItemRepository = cartItemRepository;
        this.customerService = customerService;
        this.shoppingCartRepository = shoppingCartRepository;
    }

    @Override
    public ShoppingCart addItemToCart(Product product, int quantity, Customer customer) {
        ShoppingCart cart = customer.getShoppingCart();
        if(cart==null){
                cart = new ShoppingCart();
        }
        Set<CartItem> cartItems = cart.getCartItem();
        CartItem cartItem = findCartItem(cartItems,product.getId());
        double unitPrice = product.getCostPrice();
        if (cartItems == null){
            cartItems = new HashSet<>();
            if(cartItem==null){
                cartItem = new CartItem();
                cartItem.setProduct(product);
                cartItem.setTotalPrice(quantity * product.getCostPrice());
                cartItem.setQuantity(quantity);
                cartItem.setUnitPrice(unitPrice);
                cartItem.setCart(cart);
                cartItems.add(cartItem);
                cartItemRepository.save(cartItem);
            }
        }else{
                if (cartItem == null){
                    cartItem = new CartItem();
                    cartItem.setProduct(product);
                    cartItem.setTotalPrice(quantity * product.getCostPrice());
                    cartItem.setQuantity(quantity);
                    cartItem.setCart(cart);
                    cartItem.setUnitPrice(unitPrice);
                    cartItems.add(cartItem);
                    cartItemRepository.save(cartItem);
                }else{
                    cartItem.setQuantity(cartItem.getQuantity()+quantity);
                    cartItem.setTotalPrice(cartItem.getTotalPrice()+ (quantity * product.getCostPrice()));
                    cartItemRepository.save(cartItem);
                }
            }
        cart.setCartItem(cartItems);

        int totalItems = totalItems(cart.getCartItem());
        double totalPrice = totalPrice(cart.getCartItem());

        cart.setTotalItems(totalItems);
        cart.setTotalPrice(totalPrice);
        cart.setCustomer(customer);

        return shoppingCartRepository.save(cart);
    }

    @Override
    public ShoppingCart updateItemCart(Product product, int quantity, Customer customer) {
        ShoppingCart cart = customer.getShoppingCart();
        Set<CartItem> cartItems = cart.getCartItem();
        CartItem item = findCartItem(cartItems, product.getId());
        item.setQuantity(quantity);
        item.setTotalPrice(quantity* product.getCostPrice());
        cartItemRepository.save(item);

        int totalItems = totalItems(cartItems);
        double totalPrice = totalPrice(cartItems);

        cart.setTotalItems(totalItems);
        cart.setTotalPrice(totalPrice);

        return shoppingCartRepository.save(cart);
    }

    @Override
    public ShoppingCart deleteItemFromCart(Product product, Customer customer) {

        ShoppingCart cart = customer.getShoppingCart();
        Set<CartItem> cartItemsList = cart.getCartItem();
        CartItem item = findCartItem(cartItemsList,product.getId());

        cartItemsList.remove(item);

        cartItemRepository.deleteById(item.getId());

        double totalPrice = totalPrice(cartItemsList);
        int totalItems = totalItems(cartItemsList);


        cart.setCartItem(cartItemsList);
        cart.setTotalPrice(totalPrice);
        cart.setTotalItems(totalItems);

        if (cartItemsList.isEmpty()){
            cart.setCustomer(null);
            cart.getCartItem().clear();
            cart.setTotalPrice(0.0);
            cart.setTotalItems(0);
        }

        System.out.println(cart);

        return shoppingCartRepository.save(cart);

    }

    @Override
    public ShoppingCart findCartItem(Customer customer) {
        return null;
    }

    @Override
    public void clearCart(ShoppingCart cart) {
        for (CartItem cartItem : cart.getCartItem()) {
            cartItem.setCart(null);
            cartItemRepository.deleteById(cartItem.getId());
        }
        cart.setCustomer(null);
        cart.getCartItem().clear();
        cart.setTotalItems(0);
        cart.setTotalPrice(0.0);
        shoppingCartRepository.save(cart);
    }

    @Override
    public ShoppingCart updateShoppingCartPrice(Double price, String username) {
        System.out.println(price+"updateShoppingCart");
        Customer customer = customerService.findByUsername(username);
        ShoppingCart cart = customer.getShoppingCart();
        cart.setTotalPrice(price);
        shoppingCartRepository.save(cart);
        return cart;
    }


    private CartItem findCartItem(Set<CartItem> cartItems, Long productId){
        if(cartItems == null){
            return null;
        }
        CartItem cartItem = null;
        for (CartItem item:cartItems){
            if (item.getProduct().getId() == productId){
                cartItem = item;
            }

        }
            return  cartItem;

    }
    private int totalItems(Set<CartItem> cartItems){
        int totalItems = 0;
        for (CartItem item: cartItems){
             totalItems += item.getQuantity();
        }
        return totalItems;
    }
    private double totalPrice(Set<CartItem> cartItems){
        double totalPrice = 0.0;
        for (CartItem item : cartItems){
            totalPrice += item.getTotalPrice();
        }
        return totalPrice;
    }
}

