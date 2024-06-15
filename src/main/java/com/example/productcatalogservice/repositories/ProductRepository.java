package com.example.productcatalogservice.repositories;

import com.example.productcatalogservice.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
