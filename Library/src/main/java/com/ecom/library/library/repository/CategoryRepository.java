package com.ecom.library.library.repository;

import com.ecom.library.library.models.Category;
import com.ecom.library.library.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    @Query("SELECT c FROM Category c WHERE c.is_activated=TRUE AND c.is_deleted=FALSE")
    List<Category> findAllByActivated();

    @Override
    Page<Category> findAll(Pageable pageable);

    @Query("select c from Category c where c.name ilike %?1%")
    Page<Category> searchCategory(String keyword,Pageable pageable);
}
