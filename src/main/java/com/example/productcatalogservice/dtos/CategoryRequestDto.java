package com.example.productcatalogservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequestDto {
    private String name;
    private String description;
//    private List<Product> products = new ArrayList<>();
}
