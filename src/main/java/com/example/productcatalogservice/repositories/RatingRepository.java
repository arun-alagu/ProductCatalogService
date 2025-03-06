package com.example.productcatalogservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.productcatalogservice.models.Rating;
import com.example.productcatalogservice.models.Product;


@Repository
public interface RatingRepository extends JpaRepository<Rating, Long>{
	Rating findByProduct(Product product);
}
