package com.example.productcatalogservice.services;

import com.example.productcatalogservice.models.Product;

import java.util.List;

public interface IProductService {
    List<Product> getAllProducts();
    Product getProductById(Long productId);
    Product addProduct(Product product);
    Product updateProduct(Product product, Long productId);
    Product deleteProduct(Long productId);
}
