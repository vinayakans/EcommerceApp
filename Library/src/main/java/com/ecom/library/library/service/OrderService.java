package com.ecom.library.library.service;

import com.ecom.library.library.models.*;

import java.util.List;

public interface OrderService {
    void saveOrder(Customer customer, Address address, ShoppingCart cart,String payment);
    List<Order> ListOrders();

    Order findById(Long id);

    List<Order> customerOrderList(Long customer_id);

    Order CancleProduct(Long id);
    Order returnProduct(Long id);
    void updateOrderStatus(String Order_status,Long Order_id);
    void acceptOrder(String status,Long order_id);


}
