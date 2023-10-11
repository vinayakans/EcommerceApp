package com.ecom.library.library.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {

    private Long id;
    @NotEmpty
    @Size(min = 3, max = 15, message = "first name should have 3-15 characters")
    @NotEmpty
    @Pattern(regexp = "^[A-Za-z0-9\\s]+$", message = "Username must contain only alphanumeric characters.")
    private String firstname;
    @Size(min = 1, max = 15, message = "last name should have 3-15 characters")
    @Pattern(regexp = "^[A-Za-z0-9\\s]+$", message = "Username must contain only alphanumeric characters.")
    private String lastname;
    @NotEmpty(message = "PhoneNumber Cannot be Empty")
    @Min(value = 1000000000, message = "Phone number should have at least 10 digits")
    @Max(value = 9999999999L, message = "Phone number should have at most 10 digits")
    private String phonenumber;
    @NotEmpty(message = "User Cannot be Empty")
    private String username;
    @Size(min = 3, max = 20, message = "password should have 3-15 characters")
    @NotEmpty
    private String password;
    @NotEmpty
    private String repeatPassword;
    private boolean is_blocked;
}