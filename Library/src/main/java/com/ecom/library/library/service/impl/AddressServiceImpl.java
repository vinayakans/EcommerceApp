package com.ecom.library.library.service.impl;

import com.ecom.library.library.dto.AddressDto;
import com.ecom.library.library.models.Address;
import com.ecom.library.library.models.Customer;
import com.ecom.library.library.repository.AddressRepository;
import com.ecom.library.library.service.AddressService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

   private AddressRepository addressRepository;

    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public Address AddAddress(AddressDto addressDto, Customer customer) {
        Address address = new Address();
        address.setAddressLine1(addressDto.getAddressLine1());
        address.setAddressLine2(addressDto.getAddressLine2());
        address.setCity(addressDto.getCity());
        address.setPinCode(addressDto.getPinCode());
        address.setCountry(addressDto.getCountry());
        address.setState(addressDto.getState());
        address.setPhoneNumber(addressDto.getPhoneNumber());
        address.setCustomer(customer);
        address.setDeleted(false);
        addressRepository.save(address);
        return address;
    }

    @Override
    public List<Address> findAllById(Long Id) {
        List<Address> addressList = addressRepository.findAllById(Id);
        return addressList;
    }

    @Override
    public Address findById(Long id) {
        Address address = addressRepository.getReferenceById(id);
        return address;
    }

    @Override
    public Address updateAddress(Address address,Long id) {
        Address address1 = addressRepository.getReferenceById(id);
        address1.setAddressLine1(address.getAddressLine1());
        address1.setAddressLine2(address.getAddressLine2());
        address1.setCountry(address.getCountry());
        address1.setState(address.getState());
        address1.setPhoneNumber(address.getPhoneNumber());
        address1.setPinCode(address.getPinCode());
        return addressRepository.save(address1);
    }

    @Override
    public void deleteById(Long id) {
        Address address = addressRepository.getReferenceById(id);
        address.setDeleted(true);
        try{
            addressRepository.save(address);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }


}
