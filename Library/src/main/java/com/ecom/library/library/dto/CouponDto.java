package com.ecom.library.library.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
@Data
public class CouponDto {
    private Long id;
    @NotBlank
    @NotEmpty
    private String couponCode;

    @NotBlank
    @NotEmpty
    private String description;

    @Min(value = 1)
    @Max(value = 100)
    private int percentage;

    @Min(value = 1)
    @Max(value = 100000)
    private int maxOff;

    @Min(value = 1)
    @Max(value = 100)
    private int count;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate expDate;

    @Min(value = 1)
    @Max(value = 100000)
    private int minAmount;

    private boolean enabled;
}
