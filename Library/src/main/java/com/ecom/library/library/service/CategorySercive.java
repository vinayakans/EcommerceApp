package com.ecom.library.library.service;

import com.ecom.library.library.dto.CategoryDto;
import com.ecom.library.library.models.Category;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CategorySercive {
    List<Category> findAll();

    void save(Category category);
    Category findById(Long id);

    Category update(Category category);
    void deleteById(Long id);
    void enabledById(Long id);
    List<Category> findAllByActivated();
    Page<Category> pageCategory(int pageNo);
    Page<Category> searchCategory(String keyword,int pageNo);
}
