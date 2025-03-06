package com.example.productcatalogservice.dtos;

import java.util.Optional;

import com.example.productcatalogservice.models.Rating;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RatingRequestDto {
    private Float rate;
    private Integer count;
    
    public Rating getRating(RatingRequestDto dto) {
    	Rating rating = new Rating();
        Optional.ofNullable(dto.getRate()).ifPresent(rating::setRate);
        Optional.ofNullable(dto.getCount()).ifPresent(rating::setCount);
        return rating;
    }
}
