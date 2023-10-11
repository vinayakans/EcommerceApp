package com.ecom.library.library.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
public class AdminDto {
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Username must contain only alphanumeric characters.")
    @NotEmpty
    @Size(min = 3,max = 10, message = "Invalid name(3-10 character required)")
    private String firstname;
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Username must contain only alphanumeric characters.")
    @NotEmpty
    @Size(min = 1,max = 10, message = "Invalid name(1-10 character required)")
    private String lastname;
    @NotEmpty(message = "Username cannot be empty")
    private String username;
    @NotEmpty
    @Size(min = 5,max = 15,message = "password cannot be empty")
    private String password;
    @NotEmpty
    private String repeatpassword;

}
