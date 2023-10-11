package com.ecom.library.library.dto;

import com.ecom.library.library.models.Customer;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AddressDto {
    @Pattern(regexp = "^[a-zA-Z0-9\\s\\-_]*$", message = "Username must contain only alphanumeric characters.")
    @Column(name="addLine1")
    private String addressLine1;
    @Pattern(regexp = "^[a-zA-Z0-9\\s\\-_]*$", message = "Username must contain only alphanumeric characters.")
    @Column(name="addLine2")
    private String addressLine2;

    private String city;
    @NotEmpty(message = "Cannot be null")
    @Min(value = 100000,message = "Check your pin code")
    @Max(value = 999999,message = "Check your pincode ")
    private String pinCode;

    private String state;

    @NotEmpty(message = "PhoneNumber Cannot be Empty")
    @Min(value = 1000000000, message = "Phone number should have at least 10 digits")
    @Max(value = 9999999999L, message = "Phone number should have at most 10 digits")
    private String phoneNumber;
    @NotEmpty
    private String country;

    private Customer customer;

    private boolean isDeleted;
}
