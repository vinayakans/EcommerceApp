package com.ecom.library.library.repository;

import com.ecom.library.library.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    @Query(value = "SELECT * FROM product WHERE is_activated = false AND is_deleted = false",nativeQuery = true)
    List<Product> findAllByActive();

    @Query(value = "SELECT * FROM product WHERE is_deleted = false",nativeQuery = true)
    List<Product> findAllByNotDeleted();

    @Query("select p from Product p where p.is_deleted = false")
    Page<Product> pageProduct(Pageable pageable);

    @Query("select  p from Product p where p.is_deleted=false and p.description ilike %?1% or p.name ilike %?1%")
    Page<Product> searchProducts(String keyword,Pageable pageable);

    Product findById(long id);
    @Query(value ="SELECT * FROM product WHERE category_id = :id and is_activated = false",nativeQuery = true)
    List<Product> findBYCategoryId(@Param("id") Long id);
}
