package com.example.productcatalogservice.dtos;

import com.example.productcatalogservice.models.Category;
import com.example.productcatalogservice.models.Product;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
public class ProductRequestDto {
    private String title;
    private Double price;
    private String description;
    private String imageUrl;
    private CategoryRequestDto category;

    public static Product getProduct(ProductRequestDto productRequestDto){
        Product product = new Product();

        Optional.ofNullable(productRequestDto.getTitle()).ifPresent(product::setTitle);
        Optional.ofNullable(productRequestDto.getPrice()).ifPresent(product::setPrice);
        Optional.ofNullable(productRequestDto.getDescription()).ifPresent(product::setDescription);
        Optional.ofNullable(productRequestDto.getImageUrl()).ifPresent(product::setImageUrl);

        Optional.ofNullable(productRequestDto.getCategory()).ifPresent(categoryRequestDto -> {
            Category category = new Category();
            Optional.ofNullable(categoryRequestDto.getName()).ifPresent(category::setName);
            Optional.ofNullable(categoryRequestDto.getDescription()).ifPresent(category::setDescription);
            product.setCategory(category);
        });

        return product;
    }
}
