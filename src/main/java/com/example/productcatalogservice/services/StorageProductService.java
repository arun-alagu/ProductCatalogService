package com.example.productcatalogservice.services;

import com.example.productcatalogservice.models.Product;
import com.example.productcatalogservice.repositories.ProductRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Primary
public class StorageProductService implements  IProductService {

    private final ProductRepository productRepository;

    public StorageProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(()-> new RuntimeException("Product not found"));
    }

    @Override
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Product product, Long productId) {
        Product updatedProduct = getProductById(productId);

        Optional.ofNullable(product.getName()).ifPresent(updatedProduct::setName);
        Optional.ofNullable(product.getDescription()).ifPresent(updatedProduct::setDescription);
        Optional.ofNullable(product.getPrice()).ifPresent(updatedProduct::setPrice);
        Optional.ofNullable(product.getImageUrl()).ifPresent(updatedProduct::setImageUrl);
        Optional.ofNullable(product.getCategory()).ifPresent(updatedProduct::setCategory);
        Optional.ofNullable(product.getRating()).ifPresent(updatedProduct::setRating);

        return productRepository.save(updatedProduct);
    }

    @Override
    public Product deleteProduct(Long productId) {
        Product deletedProduct = getProductById(productId);
        productRepository.deleteById(productId);
        return deletedProduct;
    }
}
