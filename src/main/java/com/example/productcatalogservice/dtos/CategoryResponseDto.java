package com.example.productcatalogservice.dtos;

import java.util.Optional;

import com.example.productcatalogservice.models.Category;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryResponseDto {
    private Long id;
    private String name;
    private String description;
    
    public static CategoryResponseDto getProductResponseDto(Category category){
    	CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        Optional.ofNullable(category.getId()).ifPresent(categoryResponseDto::setId);
        Optional.ofNullable(category.getName()).ifPresent(categoryResponseDto::setName);
        Optional.ofNullable(category.getDescription()).ifPresent(categoryResponseDto::setDescription);

        return categoryResponseDto;
    }
}
