package com.ecom.library.library.service.impl;

import com.ecom.library.library.models.OrderDetails;
import com.ecom.library.library.repository.OrderDetailsRepository;
import com.ecom.library.library.service.OrderDetailService;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailsServiceImpl implements OrderDetailService {
    private OrderDetailsRepository orderDetailsRepository;

    public OrderDetailsServiceImpl(OrderDetailsRepository orderDetailsRepository) {
        this.orderDetailsRepository = orderDetailsRepository;
    }

    @Override
    public List<OrderDetails> findAllById(Long id) {
        return null;
    }

    @Override
    public List<OrderDetails> orderDetailsByOrderId(Long id) {
        List<OrderDetails> orderDetailsList = orderDetailsRepository.findAllByOrderId(id);
        return orderDetailsList;
    }
}
