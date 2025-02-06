package com.example.productcatalogservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RatingResponseDto {
    private Float rate;
    private Integer count;
}
