package com.ecom.library.library.service.impl;

import com.ecom.library.library.dto.CustomerDto;
import com.ecom.library.library.models.Customer;
import com.ecom.library.library.repository.CustomerRepository;
import com.ecom.library.library.repository.RoleRepository;
import com.ecom.library.library.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {
    private CustomerRepository customerRepository;

    private RoleRepository roleRepository;


    public CustomerServiceImpl(CustomerRepository customerRepository, RoleRepository roleRepository) {

        this.customerRepository = customerRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public Customer save(CustomerDto customerDto) {
         Customer customer = new Customer();
         customer.setFirstName(customerDto.getFirstname());
         customer.setLastName(customerDto.getLastname());
         customer.setUsername(customerDto.getUsername());
         customer.setPhoneNumber(customerDto.getPhonenumber());
         customer.setPassword(customerDto.getPassword());
         customer.set_blocked(false);
         customer.setRoles(Arrays.asList(roleRepository.findByName("CUSTOMER")));

        return customerRepository.save(customer);
    }

    @Override
    public Customer findByUsername(String username) {
        return customerRepository.findByUsername(username);
    }

    @Override
    public List<CustomerDto> findAll() {
        List<CustomerDto>  customerDtoList = new ArrayList<>();
        List<Customer> customers = customerRepository.findAll();
        for(Customer customer:customers){
            CustomerDto customerDto = new CustomerDto();
            customerDto.setId(customer.getId());
            customerDto.setFirstname(customer.getFirstName());
            customerDto.setLastname(customer.getLastName());
            customerDto.setUsername(customer.getUsername());
            customerDto.setPhonenumber(customer.getPhoneNumber());
            customerDto.set_blocked(customer.is_blocked());
            customerDtoList.add(customerDto);
        }
        return customerDtoList;
    }

    @Override
    public void blockUser(Long id) {
       Customer customer = customerRepository.getReferenceById(id);
       if(customer.is_blocked()){
           customer.set_blocked(false);
       }else customer.set_blocked(true);

    }
}
