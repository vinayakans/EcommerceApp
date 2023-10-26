package com.ecom.library.library.service.impl;

import com.ecom.library.library.models.*;
import com.ecom.library.library.repository.OrderDetailsRepository;
import com.ecom.library.library.repository.OrderRepository;
import com.ecom.library.library.repository.ProductRepository;
import com.ecom.library.library.service.OrderService;
import com.ecom.library.library.service.ShoppingCartServices;
import jdk.swing.interop.SwingInterOpUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private OrderRepository orderRepository;
    private ProductRepository productRepository;
    private OrderDetailsRepository orderDetailsRepository;

    public OrderServiceImpl(OrderRepository orderRepository, ProductRepository productRepository,
                            OrderDetailsRepository orderDetailsRepository, ShoppingCartServices shoppingCartServices) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderDetailsRepository = orderDetailsRepository;
        this.shoppingCartServices = shoppingCartServices;
    }

    private ShoppingCartServices shoppingCartServices;

    @Override
    public Order saveOrder(Customer customer, Address address, ShoppingCart cart,String payment) {
        Order order = new Order();
        order.setOrderDate(new Date());
        order.setCustomer(customer);
        order.setOrderStatus("pending");
        order.setPaymentMethod(payment);
        order.setShippingFee(0.0);
        order.setShippingAddress(address);
        System.out.println("1");
        order.setTotalPrice(cart.getTotalPrice());
        order.setNotes("thank you ");
        order.setOrderDelivered(false);

        List<OrderDetails> orderDetailsList = new ArrayList<>();
        for (CartItem item: cart.getCartItem()){
            Product product = item.getProduct();
            int currentQuantity = product.getCurrentQuantity();
            product.setCurrentQuantity(currentQuantity - item.getQuantity());
            productRepository.save(product);

            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setOrder(order);
            orderDetails.setProduct(item.getProduct());
            orderDetails.setQuantity(item.getQuantity());
            orderDetails.setUnitPrice(item.getUnitPrice());
            orderDetails.setTotalPrice(item.getQuantity()*item.getUnitPrice());
            orderDetailsRepository.save(orderDetails);
            orderDetailsList.add(orderDetails);
        }
        order.setOrderDetailsList(orderDetailsList);
        if (payment.equals("cod")){

            order.setOrderStatus("pending");
            shoppingCartServices.clearCart(cart);

        } else if (payment.equals("wallet")) {

            order.setOrderStatus("pending");
            shoppingCartServices.clearCart(cart);
        }
      return orderRepository.save(order);
    }

    @Override
    public List<Order> ListOrders() {
        List<Order> orderList = orderRepository.findAll();
        return orderList;
    }


    @Override
    public Order findById(Long id) {
        return orderRepository.getReferenceById(id);
    }

    @Override
    public List<Order> customerOrderList(Long customer_id) {
        List<Order> customerOrderList = orderRepository.customerOrderList(customer_id);
        return customerOrderList;
    }

    @Override
    public Order CancleProduct(Long id) {
        Order order = orderRepository.getReferenceById(id);
        List<OrderDetails> orderDetailsList = order.getOrderDetailsList();
            for (OrderDetails orderDetails:orderDetailsList){
                Product product = orderDetails.getProduct();
                if (product!=null){
                    int currentQuantity = product.getCurrentQuantity();
                    product.setCurrentQuantity(currentQuantity+ orderDetails.getQuantity());
                    productRepository.save(product);
                }
            }
        order.setOrderStatus("cancel");
        orderRepository.save(order);
        return null;
    }

    @Override
    public Order returnProduct(Long id) {
        Order order = orderRepository.getReferenceById(id);
        List<OrderDetails> orderDetailsList = order.getOrderDetailsList();
            for (OrderDetails orderDetails : orderDetailsList){
                Product product = orderDetails.getProduct();
                if (product!=null){
                    int currentQuantity = product.getCurrentQuantity();
                    product.setCurrentQuantity(currentQuantity+ orderDetails.getQuantity());
                    productRepository.save(product);
                }
            }
        order.setPaymentStatus("processing");
        order.setOrderStatus("returned");

        orderRepository.save(order);
        return null;
    }

    @Override
    public void updateOrderStatus(String Order_status, Long order_id) {
        if(order_id != 0) {
            Order order = orderRepository.getReferenceById(order_id);
             if(Order_status.equals("SHIPPED")){
                order.setOrderStatus(Order_status);
                orderRepository.save(order);
            }else if(Order_status.equals("DELIVERED")){
                order.setOrderStatus(Order_status);
                 order.setOrderDelivered(true);
                if(order.getPaymentMethod().equals("cod")){
                    order.setPaymentStatus("paid");
                }
                orderRepository.save(order);
            }
        }
    }
    @Override
    public void acceptOrder(String status,Long order_id) {
        Order order = orderRepository.getReferenceById(order_id);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(order.getOrderDate());
        calendar.add(calendar.DAY_OF_MONTH,5);
        Date ofFuture = calendar.getTime();
        order.setDelivaryDate(ofFuture);
        order.setOrderStatus(status);
        orderRepository.save(order);
    }

    @Override
    public int totalOrders() {

        return orderRepository.orderCount();
    }

    @Override
    public int totalPending() {
        return orderRepository.pendingCount();
    }

    @Override
    public Order findOrderById(Long id) {
        return orderRepository.getReferenceById(id);
    }

    @Override
    public void updatePayment(Order order, boolean status) {
        if (status){
            order.setPaymentStatus("paid");
            orderRepository.save(order);
        }else {
            order.setPaymentStatus("failed");
            orderRepository.save(order);
        }
    }

}
