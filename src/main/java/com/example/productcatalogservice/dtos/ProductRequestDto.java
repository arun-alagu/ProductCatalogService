package com.example.productcatalogservice.dtos;

import com.example.productcatalogservice.models.Product;
import com.example.productcatalogservice.models.Rating;

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

    public static Product getProduct(ProductRequestDto productRequestDto){
        Product product = new Product();

        Optional.ofNullable(productRequestDto.getTitle()).ifPresent(product::setTitle);
        Optional.ofNullable(productRequestDto.getPrice()).ifPresent(product::setPrice);
        Optional.ofNullable(productRequestDto.getDescription()).ifPresent(product::setDescription);
        Optional.ofNullable(productRequestDto.getImageUrl()).ifPresent(product::setImageUrl);
        Rating rating = new Rating();
        product.setRating(rating);

        return product;
    }
}
