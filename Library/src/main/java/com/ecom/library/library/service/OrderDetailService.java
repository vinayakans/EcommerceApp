package com.ecom.library.library.service;

import com.ecom.library.library.models.OrderDetails;

import java.util.List;

public interface OrderDetailService {
   List<OrderDetails> findAllById(Long id);

   List<OrderDetails>orderDetailsByOrderId(Long id);
}
