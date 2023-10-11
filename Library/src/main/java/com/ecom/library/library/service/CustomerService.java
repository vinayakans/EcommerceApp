package com.ecom.library.library.service;


import com.ecom.library.library.dto.CustomerDto;
import com.ecom.library.library.models.Customer;

import java.util.List;

public interface CustomerService {

    Customer save(CustomerDto customerDto);

    Customer findByUsername(String username);

   List<CustomerDto> findAll();

   void blockUser(Long id);
}