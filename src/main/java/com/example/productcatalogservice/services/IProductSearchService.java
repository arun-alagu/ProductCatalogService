package com.example.productcatalogservice.services;

import com.example.productcatalogservice.models.Product;

import java.util.List;

public interface IProductSearchService {
    List<Product> searchProducts(String keyword, int pageNumber, int pageSize);
}
