package com.ecom.library.library.service;

import com.ecom.library.library.dto.ProductDto;
import com.ecom.library.library.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    List<ProductDto> findAll();

    Product save(List<MultipartFile> imageProduct,ProductDto productDto);

    Product update(List<MultipartFile> imageProduct,ProductDto productDto,Long id);
    void softDeleteById(long id);
    ProductDto getById(Long id);
    Product getProductById(Long id);
    void enableProduct(Long id);

    Product findById(Long id);

    List<Product> findAllByActive();

    List<Product> findAllNotDeleted();

    Page<Product> pageProducts(int pageNo);

    Page<Product> searchProducts(String keyword,int pageNo);

    List <Product> findByCategory(Long id);


}
