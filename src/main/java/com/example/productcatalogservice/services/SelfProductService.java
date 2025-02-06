package com.example.productcatalogservice.services;

import com.example.productcatalogservice.dtos.FakeStoreProductResponseDto;
import com.example.productcatalogservice.dtos.ProductResponseDto;
import com.example.productcatalogservice.exceptions.ProductNotFoundException;
import com.example.productcatalogservice.models.Product;
import com.example.productcatalogservice.repositories.ProductRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service(value = "SelfProductService")
@Primary
public class SelfProductService implements  IProductService {

    private final ProductRepository productRepository;
    private final RedisTemplate<String, Object> redisTemplateObject;
    private final RedisTemplate<String, List<Object>> redisTemplateObjects;

    public SelfProductService(ProductRepository productRepository, RedisTemplate<String, Object> redisTemplateObject, RedisTemplate<String, List<Object>> redisTemplateObjects) {
        this.productRepository = productRepository;
        this.redisTemplateObject = redisTemplateObject;
        this.redisTemplateObjects = redisTemplateObjects;
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> products = (List<Product>) redisTemplateObjects.opsForHash().get("PRODUCTS", "PRODUCTS_");
        if (products != null && !products.isEmpty() && redisTemplateObjects.getExpire("PRODUCTS") > System.currentTimeMillis()) return products;
//        if (products != null && !products.isEmpty()) return products;
        products = productRepository.findAll();
        redisTemplateObjects.expire("PRODUCTS", 30, TimeUnit.SECONDS);
        redisTemplateObjects.opsForHash().put("PRODUCTS", "PRODUCTS_", products);
        return products;
//        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long productId) throws ProductNotFoundException {
//        return productRepository.findById(productId)
//                .orElseThrow(()-> new ProductNotFoundException(productId ,"Product not found"));
        Product product = (Product) redisTemplateObject.opsForHash().get("PRODUCT", "PRODUCT_" +productId);
        if (product != null) return product;

        product = productRepository.findById(productId)
                .orElseThrow(()-> new ProductNotFoundException(productId ,"Product not found"));
        redisTemplateObject.expire("PRODUCT", 30, TimeUnit.SECONDS);
        redisTemplateObject.opsForHash().put("PRODUCT", "PRODUCT_" + productId, product);
        return product;
    }

    @Override
    public Product addProduct(Product product) throws ProductNotFoundException {
        if(product == null) throw new IllegalArgumentException("Product cannot be null");
        return Optional.of(productRepository.save(product)).orElseThrow(
                ()-> new ProductNotFoundException(product.getId(),"Product not found")
        );
    }

    @Override
    public Product updateProductById(Product product, Long productId) throws ProductNotFoundException {
        if(product == null) throw new IllegalArgumentException("Product cannot be null");
        Product updatedProduct = getProductById(productId);

        Optional.ofNullable(product.getTitle()).ifPresent(updatedProduct::setTitle);
        Optional.ofNullable(product.getDescription()).ifPresent(updatedProduct::setDescription);
        Optional.ofNullable(product.getPrice()).ifPresent(updatedProduct::setPrice);
        Optional.ofNullable(product.getImageUrl()).ifPresent(updatedProduct::setImageUrl);
        Optional.ofNullable(product.getCategory()).ifPresent(updatedProduct::setCategory);
        Optional.ofNullable(product.getRating()).ifPresent(updatedProduct::setRating);

        return Optional.of(productRepository.save(updatedProduct)).orElseThrow(
                ()-> new ProductNotFoundException(productId, "Product not found")
        );
    }

    @Override
    public Product deleteProductById(Long productId) throws ProductNotFoundException {
        Product deletedProduct = getProductById(productId);
        productRepository.deleteById(productId);
        return deletedProduct;
    }
}
