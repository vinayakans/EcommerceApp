package com.ecom.library.library.service;

import com.ecom.library.library.dto.AddressDto;
import com.ecom.library.library.models.Address;
import com.ecom.library.library.models.Customer;

import java.util.List;

public interface AddressService {
    public Address AddAddress(AddressDto addressDto, Customer customer);

    public List<Address> findAllById(Long Id);

    public Address findById(Long id);

    Address updateAddress(Address address,Long id);

    void deleteById(Long id);
}
