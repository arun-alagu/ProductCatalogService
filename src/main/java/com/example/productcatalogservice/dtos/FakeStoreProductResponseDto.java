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
public class FakeStoreProductResponseDto {
    private Long id;
    private String title;
    private Double price;
    private String description;
    private String category;
    private String image;
    private FakeStoreRatingDto rating;

}
