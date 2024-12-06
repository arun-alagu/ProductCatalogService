package com.example.productcatalogservice.dtos;

import com.example.productcatalogservice.models.Product;
import com.example.productcatalogservice.models.Rating;
import lombok.Getter;
import lombok.Setter;

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
    private CategoryResponseDto category;
    private List<RatingResponseDto> rating;

    public static ProductResponseDto getProductResponseDto(Product product){
        ProductResponseDto productResponseDto = new ProductResponseDto();

        Optional.ofNullable(product.getId()).ifPresent(productResponseDto::setId);
        Optional.ofNullable(product.getTitle()).ifPresent(productResponseDto::setTitle);
        Optional.ofNullable(product.getDescription()).ifPresent(productResponseDto::setDescription);
        Optional.ofNullable(product.getPrice()).ifPresent(productResponseDto::setPrice);
        Optional.ofNullable(product.getImageUrl()).ifPresent(productResponseDto::setImageUrl);

        Optional.ofNullable(product.getCategory()).ifPresent(category -> {
            CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
            Optional.ofNullable(category.getId()).ifPresent(categoryResponseDto::setId);
            Optional.ofNullable(category.getName()).ifPresent(categoryResponseDto::setName);
            productResponseDto.setCategory(categoryResponseDto);
        });

        Optional.ofNullable(product.getRating()).ifPresent(ratings -> {
            List<RatingResponseDto> ratingResponseDtoList = new LinkedList<>();
            for(Rating rating : ratings){
                RatingResponseDto ratingResponseDto = new RatingResponseDto();
                Optional.ofNullable(rating.getRate()).ifPresent(ratingResponseDto::setRate);
                ratingResponseDtoList.add(ratingResponseDto);
            }
            productResponseDto.setRating(ratingResponseDtoList);
        });

        return productResponseDto;
    }
}
