package com.example.productcatalogservice.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductResponseDto {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private String imageUrl;
    private CategoryResponseDto category;
    private List<RatingResponseDto> rating;
}
