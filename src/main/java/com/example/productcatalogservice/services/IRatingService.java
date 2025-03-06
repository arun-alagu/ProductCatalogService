package com.example.productcatalogservice.services;

import com.example.productcatalogservice.exceptions.RatingNotFoundException;
import com.example.productcatalogservice.models.Product;
import com.example.productcatalogservice.models.Rating;

import java.util.List;

public interface IRatingService {
    Rating getRatingById(Long ratingId) throws RatingNotFoundException;
    Rating getRatingByProduct(Product product) throws RatingNotFoundException;
    Rating addRating(Rating rating) throws RatingNotFoundException;
    Rating updateRating(Rating rating, Long RatingId) throws RatingNotFoundException;
    Rating deleteRating(Long ratingId) throws RatingNotFoundException;
}
