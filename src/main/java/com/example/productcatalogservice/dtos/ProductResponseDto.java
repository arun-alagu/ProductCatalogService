package com.example.productcatalogservice.dtos;

import com.example.productcatalogservice.models.Product;
import com.example.productcatalogservice.models.Rating;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class ProductResponseDto {
    private Long id;
    private String title;
    private String description;
    private Double price;
    private String imageUrl;
    private List<CategoryResponseDto> categories = new ArrayList<>();
    private RatingResponseDto rating;

    public static ProductResponseDto getProductResponseDto(Product product){
        ProductResponseDto productResponseDto = new ProductResponseDto();

        Optional.ofNullable(product.getId()).ifPresent(productResponseDto::setId);
        Optional.ofNullable(product.getTitle()).ifPresent(productResponseDto::setTitle);
        Optional.ofNullable(product.getDescription()).ifPresent(productResponseDto::setDescription);
        Optional.ofNullable(product.getPrice()).ifPresent(productResponseDto::setPrice);
        Optional.ofNullable(product.getImageUrl()).ifPresent(productResponseDto::setImageUrl);

        Optional.ofNullable(product.getCategories()).ifPresent((categories)->{
        	categories.forEach(category -> {
        		CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
                Optional.ofNullable(category.getId()).ifPresent(categoryResponseDto::setId);
                Optional.ofNullable(category.getName()).ifPresent(categoryResponseDto::setName);
                productResponseDto.categories.add(categoryResponseDto);
        	});
        });

//        Optional.ofNullable(product.getRating()).ifPresent(rating -> {
//                RatingResponseDto ratingResponseDto = new RatingResponseDto();
//                Optional.ofNullable(rating.getRate()).ifPresent(ratingResponseDto::setRate);
//                Optional.ofNullable(rating.getCount()).ifPresent(ratingResponseDto::setCount);
//            productResponseDto.setRating(ratingResponseDto);
//        });

        return productResponseDto;
    }
}
