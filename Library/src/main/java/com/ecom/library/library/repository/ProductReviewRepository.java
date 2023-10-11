package com.ecom.library.library.repository;

import com.ecom.library.library.models.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReview,Long> {
    @Query(value = "SELECT * FROM product_review where product_id =:id",nativeQuery = true)
    List<ProductReview> findByProductId(@Param("id")Long id);
}
