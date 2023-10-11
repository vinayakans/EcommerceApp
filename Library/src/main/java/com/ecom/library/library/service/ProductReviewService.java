package com.ecom.library.library.service;

import com.ecom.library.library.models.Customer;
import com.ecom.library.library.models.Product;
import com.ecom.library.library.models.ProductReview;

import java.util.List;

public interface ProductReviewService {
    void saveReview(Product product, Customer customer,String review,int rating);

   List< ProductReview >findByProductId(Long id);
}
