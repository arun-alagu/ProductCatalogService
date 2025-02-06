package com.example.productcatalogservice.services;

import com.example.productcatalogservice.clients.FakeStoreClient;
import com.example.productcatalogservice.dtos.*;
import com.example.productcatalogservice.models.Category;
import com.example.productcatalogservice.models.Product;
import com.example.productcatalogservice.models.Rating;

import com.example.productcatalogservice.repositories.ProductRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service(value = "FakeStoreProductService")
public class FakeStoreProductService implements IProductService {

    private final FakeStoreClient fakeStoreClient;
    private final RedisTemplate<String, Object> redisTemplateObject;
    private final RedisTemplate<String, List<Object>> redisTemplateObjects;

    public FakeStoreProductService(FakeStoreClient fakeStoreClient, RedisTemplate<String, Object> redisTemplate,
                                   RedisTemplate<String, List<Object>> redisTemplateObjects) {
        this.fakeStoreClient = fakeStoreClient;
        this.redisTemplateObject = redisTemplate;
        this.redisTemplateObjects = redisTemplateObjects;
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> products = (List<Product>) redisTemplateObjects.opsForHash().get("PRODUCTS", "PRODUCTS_");
        if (products != null && !products.isEmpty()) {
            return products;
        }
        FakeStoreProductResponseDto[] fakeStoreProductResponseDtos =
                fakeStoreClient.getAllProducts();
        if (fakeStoreProductResponseDtos == null) throw new RuntimeException("Products not found");
        products = new LinkedList<>();
        for (FakeStoreProductResponseDto fakeStoreProductResponseDto :
                fakeStoreProductResponseDtos) {
            products.add(getProduct(fakeStoreProductResponseDto));
        }

        redisTemplateObjects.opsForHash().put("PRODUCTS", "PRODUCTS_", products);
        return products;
    }

    @Override
    public Product getProductById(Long productId) {
//        return getProduct(fakeStoreClient.getProduct(productId));
        Product product = (Product) redisTemplateObject.opsForHash().get("PRODUCT", "PRODUCT_" +productId);
        if (product != null) {
            return product;
        }
        FakeStoreProductResponseDto fakeStoreProductResponseDto = fakeStoreClient.getProduct(productId);
        if (fakeStoreProductResponseDto == null) throw new RuntimeException("Product not found");
        product = getProduct(fakeStoreProductResponseDto);
        redisTemplateObject.opsForHash().put("PRODUCT", "PRODUCT_" + productId, product);
        return product;
    }

    @Override
    public Product addProduct(Product product) {
        return getProduct(fakeStoreClient
                .addProduct(getFakeStoreProductRequestDto(product)));
    }

    @Override
    public Product updateProductById(Product product, Long productId) {
        return getProduct(fakeStoreClient
                .updateProduct(getFakeStoreProductRequestDto(product),productId));
    }

    @Override
    public Product deleteProductById(Long productId) {
        return getProduct(fakeStoreClient.deleteProduct(productId));
    }

    private Product getProduct(FakeStoreProductResponseDto fakeStoreProductResponseDto) {
        Product product = new Product();
        Optional.ofNullable(fakeStoreProductResponseDto.getId())
                .ifPresent(product::setId);
        Optional.ofNullable(fakeStoreProductResponseDto.getTitle())
                .ifPresent(product::setTitle);
        Optional.ofNullable(fakeStoreProductResponseDto.getPrice())
                .ifPresent(product::setPrice);
        Optional.ofNullable(fakeStoreProductResponseDto.getDescription())
                .ifPresent(product::setDescription);
        Optional.ofNullable(fakeStoreProductResponseDto.getImage())
                .ifPresent(product::setImageUrl);

        Optional.ofNullable(fakeStoreProductResponseDto.getCategory())
                .ifPresent(categoryName -> {
            Category category = new Category();
            category.setName(categoryName);
            product.setCategory(category);
        });

        Optional.ofNullable(fakeStoreProductResponseDto.getRating())
                .ifPresent(fakeStoreRatingDto -> {
                        Rating rating = new Rating();
                        Optional.ofNullable(fakeStoreRatingDto.getRate()).ifPresent(rating::setRate);
                        Optional.ofNullable(fakeStoreRatingDto.getCount()).ifPresent(rating::setCount);
                    product.setRating(rating);
                });

        return product;
    }


    private FakeStoreProductRequestDto getFakeStoreProductRequestDto(Product product) {
        FakeStoreProductRequestDto fakeStoreProductRequestDto =
                new FakeStoreProductRequestDto();
        Optional.ofNullable(product.getTitle())
                .ifPresent(fakeStoreProductRequestDto::setTitle);
        Optional.ofNullable(product.getDescription())
                .ifPresent(fakeStoreProductRequestDto::setDescription);
        Optional.ofNullable(product.getPrice())
                .ifPresent(fakeStoreProductRequestDto::setPrice);
        Optional.ofNullable(product.getImageUrl())
                .ifPresent(fakeStoreProductRequestDto::setImage);

        Optional.ofNullable(product.getCategory()).flatMap(
                category -> Optional.ofNullable(category.getName()))
                .ifPresent(fakeStoreProductRequestDto::setCategory);

        return fakeStoreProductRequestDto;
    }



}
