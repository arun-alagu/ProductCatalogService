package com.example.productcatalogservice.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class SeedProductDto {
    private Long id;
    private String title;
    private String description;
    private Double price;
    private String imageUrl;
    private Set<String> categories = new HashSet<>();
    private RatingRequestDto rating;

}
