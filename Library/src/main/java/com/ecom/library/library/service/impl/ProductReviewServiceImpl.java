package com.ecom.library.library.service.impl;

import com.ecom.library.library.models.Customer;
import com.ecom.library.library.models.Product;
import com.ecom.library.library.models.ProductReview;
import com.ecom.library.library.repository.ProductRepository;
import com.ecom.library.library.repository.ProductReviewRepository;
import com.ecom.library.library.service.ProductReviewService;
import org.hibernate.LazyInitializationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductReviewServiceImpl implements ProductReviewService {
    private ProductReviewRepository productReviewRepository;

    public ProductReviewServiceImpl(ProductReviewRepository productReviewRepository) {
        this.productReviewRepository = productReviewRepository;
    }

    @Override
    public void saveReview(Product product, Customer customer, String review, int rating) {
        ProductReview productReview = new ProductReview();
        productReview.setReview(review);
        productReview.setCustomer(customer);
        productReview.setProduct(product);
        productReview.setRating(rating);

        productReviewRepository.save(productReview);
    }

    @Override
    public List<ProductReview> findByProductId(Long id) {
        List<ProductReview> productReviewList = productReviewRepository.findByProductId(id);
        return productReviewList;
    }
}
