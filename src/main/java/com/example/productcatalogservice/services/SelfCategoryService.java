package com.example.productcatalogservice.services;

import com.example.productcatalogservice.exceptions.CategoryNotFoundException;
import com.example.productcatalogservice.models.Category;
import com.example.productcatalogservice.repositories.CategoryRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service(value = "SelfCategoryService")
public class SelfCategoryService implements ICategoryService {
	private final CategoryRepository categoryRepository;

    public SelfCategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	@Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Category getCategoryById(Long categoryId) throws CategoryNotFoundException {
    	
        return categoryRepository.findById(categoryId).orElseThrow(()->
        new CategoryNotFoundException("Category: "+categoryId+" Not Found!"));
    }

    @Override
    public Category addCategory(Category category) throws CategoryNotFoundException {
    	if(category == null) throw new IllegalArgumentException("Category details cannot be empty");
    	
    	try {
    	Category old = getCategoryByName(category.getName());
    	return old;
    	} catch (CategoryNotFoundException e) {
    		return  Optional.ofNullable(categoryRepository.save(category))
            		.orElseThrow(()-> new CategoryNotFoundException("Category not created!"));
    	}
    }

    @Override
    public Category updateCategory(Category category, Long categoryId) throws CategoryNotFoundException {
    	if(category == null) throw new IllegalArgumentException("Category details cannot be empty");
    	Category updateCategory = getCategoryById(categoryId);
    	
    	Optional.ofNullable(category.getName()).ifPresent(updateCategory::setName);
    	Optional.ofNullable(category.getDescription()).ifPresent(updateCategory::setDescription);
    	Optional.ofNullable(category.getProducts()).ifPresent(products -> {
    		products.forEach(updateCategory.getProducts()::add);
    	});
    	return addCategory(updateCategory);
    }

    @Override
    public Category deleteCategory(Long categoryId) throws CategoryNotFoundException {
    	 Category deletedCategory = getCategoryById(categoryId);
         categoryRepository.deleteById(categoryId);
         return deletedCategory;
    }
    
    @Override
    public Category getCategoryByName(String cName) throws CategoryNotFoundException {
    	if(cName == null) throw new IllegalArgumentException("Category Name cannot be empty");
    	return Optional.ofNullable(categoryRepository.findByName(cName))
    			.orElseThrow(()-> new CategoryNotFoundException("Category: "+cName+" Not Found!"));
    }
    
}
