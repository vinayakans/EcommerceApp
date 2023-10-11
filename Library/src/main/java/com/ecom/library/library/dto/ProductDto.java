package com.ecom.library.library.dto;

import com.ecom.library.library.models.Category;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class ProductDto {

    private Long id;

    @Pattern(regexp = "^[a-zA-Z0-9\\s\\-_]*$", message = "Name cannot contain any special characters")
    @Size(min = 3,max = 40,message = "Minimum value required")
    private String name;

    @NotEmpty
    @Column(columnDefinition = "TEXT")
    private String description;

    @Min(value = 100,message = "minimum value required")
    private double costPrice;

    private double salePrice;

  @Min(value = 1,message = "minimum value required")
  @Max(value = 1000,message = "maximum quantity is 100")
    private int currentQuantity;

    private Category category;

//    private Brand brand;

    private String productImage;

    private boolean is_activated;

    private boolean is_deleted;

    private String base64Image;

    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }

}
