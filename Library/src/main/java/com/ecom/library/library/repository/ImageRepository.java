package com.ecom.library.library.repository;

import com.ecom.library.library.models.Images;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Images,Long> {

    @Query(value = "select * from images where product_id = :id", nativeQuery = true)
    List<Images>findImagesBy(@Param("id")Long id);
}
