package com.example.productcatalogservice.configs;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.example.productcatalogservice.dtos.SeedProductDto;
import com.example.productcatalogservice.models.Category;
import com.example.productcatalogservice.models.Product;
import com.example.productcatalogservice.models.Rating;
import com.example.productcatalogservice.services.ICategoryService;
import com.example.productcatalogservice.services.IProductService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class SeedData implements CommandLineRunner{
	private final IProductService productService;
	private final ICategoryService categoryService;
	@Value("classpath:seedData.json")
	private Resource resource;
	
	public SeedData(IProductService productService, ICategoryService categoryService) {
		this.productService = productService;
		this.categoryService = categoryService;
	}

	@Override
	public void run(String... args) throws Exception {
		
		if(productService.getAllProducts().size() == 0) {
			InputStream inputStream = resource.getInputStream();
			ObjectMapper objectMapper = new ObjectMapper();
            SeedProductDto[] data = objectMapper.readValue(inputStream, SeedProductDto[].class);
            List<Product> seedProducts = new ArrayList<>();
            // Do something with the data
            for(SeedProductDto seed : data) {
            	Product product = new Product();
                Optional.ofNullable(seed.getTitle()).ifPresent(product::setTitle);
                Optional.ofNullable(seed.getPrice()).ifPresent(product::setPrice);
                Optional.ofNullable(seed.getDescription()).ifPresent(product::setDescription);
                Optional.ofNullable(seed.getImageUrl()).ifPresent(product::setImageUrl);
            	for(String category : seed.getCategories()) {
            		Category c =  new Category();
                    	c.setName(category);
                    	product.getCategories().add(categoryService.addCategory(c));
                    }
                Optional.ofNullable(seed.getRating()).ifPresent(ratingSeed -> {
                    Rating rating = ratingSeed.getRating(ratingSeed);
                    product.setRating(rating);
                    });
                seedProducts.add(product);
            	}
            productService.addProducts(seedProducts);
            }
		
		}
	}
