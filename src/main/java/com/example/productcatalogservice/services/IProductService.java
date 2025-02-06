package com.example.productcatalogservice.services;

import com.example.productcatalogservice.exceptions.ProductNotFoundException;
import com.example.productcatalogservice.models.Product;

import java.util.List;

public interface IProductService {
    List<Product> getAllProducts();
    Product getProductById(Long productId) throws ProductNotFoundException;
    Product addProduct(Product product) throws ProductNotFoundException;
    Product updateProductById(Product product, Long productId) throws ProductNotFoundException;
    Product deleteProductById(Long productId) throws ProductNotFoundException;
}
