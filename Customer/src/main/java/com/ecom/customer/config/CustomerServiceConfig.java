package com.ecom.customer.config;

import com.ecom.library.library.models.Customer;
import com.ecom.library.library.repository.CustomerRepository;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.stream.Collectors;

public class CustomerServiceConfig implements UserDetailsService {


    private CustomerRepository customerRepository;

    public CustomerServiceConfig(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer =  customerRepository.findByUsername(username);
        if(customer==null){
            throw new UsernameNotFoundException("User name not Found!!");
        }
        if(customer.is_blocked()){
            throw new LockedException("Oops!! Contact Customer care Team");
        }
        return new User(customer.getUsername(), customer.getPassword(),
                customer.getRoles()
                        .stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList())
        );
    }
}
