package com.example.productcatalogservice.services;

import com.example.productcatalogservice.models.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "SelfCategoryService")
public class SelfCategotyService implements ICategoryService {
    @Override
    public List<Category> getAllCategories() {
        return List.of();
    }

    @Override
    public Category getCategoryById(Long categoryId) {
        return null;
    }

    @Override
    public Category addCategory(Category category) {
        return null;
    }

    @Override
    public Category updateCategory(Category category, Long categoryId) {
        return null;
    }

    @Override
    public Category deleteCategory(Long categoryId) {
        return null;
    }
}
