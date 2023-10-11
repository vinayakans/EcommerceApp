package com.ecom.library.library.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CategoryDto {
    @NotEmpty
    @Pattern(regexp = "^[A-Za-z0-9\\s]+$" ,message = "cannot Contain any alphanumeric values")
    private String name;
}
