package com.example.productcatalogservice.services;

import com.example.productcatalogservice.exceptions.CategoryNotFoundException;
import com.example.productcatalogservice.models.Category;

import java.util.List;

public interface ICategoryService {
    List<Category> getAllCategories();
    Category getCategoryById(Long categoryId) throws CategoryNotFoundException;
    Category addCategory(Category category) throws CategoryNotFoundException;
    Category updateCategory(Category category, Long categoryId) throws CategoryNotFoundException;
    Category deleteCategory(Long categoryId) throws CategoryNotFoundException;
    Category getCategoryByName(String name) throws CategoryNotFoundException;
}
