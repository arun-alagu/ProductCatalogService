package com.example.productcatalogservice.services;

import com.example.productcatalogservice.models.Category;

import java.util.List;

public interface ICategoryService {
    List<Category> getAllCategories();
    Category getCategoryById(Long categoryId);
    Category addCategory(Category category);
    Category updateCategory(Category category, Long categoryId);
    Category deleteCategory(Long categoryId);
}
