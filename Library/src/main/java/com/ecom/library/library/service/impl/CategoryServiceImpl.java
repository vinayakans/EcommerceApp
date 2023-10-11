package com.ecom.library.library.service.impl;

import com.ecom.library.library.dto.CategoryDto;
import com.ecom.library.library.models.Category;
import com.ecom.library.library.repository.CategoryRepository;
import com.ecom.library.library.service.CategorySercive;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CategoryServiceImpl implements CategorySercive {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {

        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public void save(Category category) {
        String name = category.getName();
        String trimedName = name.trim();
       category.setName(trimedName);
        category.set_activated(true);
        categoryRepository.save(category);
    }
    @Override
    public Category findById(Long id) {
        return categoryRepository.findById(id).get();
    }

    @Override
    public Category update(Category category) {
        System.out.println(category.getId());
        Category updatedCategory = categoryRepository.getReferenceById(category.getId());
        updatedCategory.setName(category.getName());
        return categoryRepository.save(updatedCategory);
    }

    @Override
    public void deleteById(Long id) {
        Category category=categoryRepository.getById(id);
        category.set_deleted(true);
        category.set_activated(false);
        categoryRepository.save(category);

    }

    @Override
    public void enabledById(Long id) {

        Category category=categoryRepository.getById(id);
        category.set_activated(true);
        category.set_deleted(false);
        categoryRepository.save(category);
    }
    @Override
    public List<Category> findAllByActivated() {
        return categoryRepository.findAllByActivated();
    }
    @Override
    public Page<Category> pageCategory(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo,5);
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        return categoryPage;
    }
    @Override
    public Page<Category> searchCategory(String keyword, int pageNo) {
        Pageable pageable = PageRequest.of(pageNo,5);
        Page<Category> categories = categoryRepository.searchCategory(keyword,pageable);
        return categories;
    }
}
