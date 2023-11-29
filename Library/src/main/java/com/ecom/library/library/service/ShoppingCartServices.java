package com.ecom.library.library.service;

import com.ecom.library.library.models.Customer;
import com.ecom.library.library.models.Product;
import com.ecom.library.library.models.ShoppingCart;

public interface ShoppingCartServices {
    ShoppingCart addItemToCart(Product product, int quantity, Customer customer);
    ShoppingCart updateItemCart(Product product,int quantity,Customer customer);
    ShoppingCart deleteItemFromCart(Product product,Customer customer);

    ShoppingCart findCartItem(Customer customer);

    void clearCart(ShoppingCart cart);

    ShoppingCart updateShoppingCartPrice(Double price,String username);
}
