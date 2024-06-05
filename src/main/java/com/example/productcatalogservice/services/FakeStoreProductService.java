package com.example.productcatalogservice.services;

import com.example.productcatalogservice.clients.FakeStoreClient;
import com.example.productcatalogservice.dtos.*;
import com.example.productcatalogservice.models.Category;
import com.example.productcatalogservice.models.Product;
import com.example.productcatalogservice.models.Rating;

import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class FakeStoreProductService implements IFakeStoreProductService{

    private final FakeStoreClient fakeStoreClient;

    public FakeStoreProductService(FakeStoreClient fakeStoreClient) {
        this.fakeStoreClient = fakeStoreClient;
    }

    @Override
    public List<Product> getAllProducts() {
        FakeStoreProductResponseDto[] fakeStoreProductResponseDtos =
                fakeStoreClient.getAllProducts();

        List<Product> products = new LinkedList<>();
        if(fakeStoreProductResponseDtos != null) {
            for (FakeStoreProductResponseDto fakeStoreProductResponseDto :
                    fakeStoreProductResponseDtos) {
                products.add(getProduct(fakeStoreProductResponseDto));
            }
        }
        return products;
    }

    @Override
    public Product getProductById(Long productId) {
        return getProduct(fakeStoreClient.getProduct(productId));
    }

    @Override
    public Product addProduct(Product product) {
        return getProduct(fakeStoreClient
                .addProduct(getFakeStoreProductRequestDto(product)));
    }

    @Override
    public Product updateProduct(Product product, Long productId) {
        return getProduct(fakeStoreClient
                .updateProduct(getFakeStoreProductRequestDto(product),productId));
    }

    @Override
    public Product deleteProduct(Long productId) {
        return getProduct(fakeStoreClient.deleteProduct(productId));
    }

    private Product getProduct(FakeStoreProductResponseDto fakeStoreProductResponseDto) {
        Product product = new Product();
        Optional.ofNullable(fakeStoreProductResponseDto.getId())
                .ifPresent(product::setId);
        Optional.ofNullable(fakeStoreProductResponseDto.getTitle())
                .ifPresent(product::setName);
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
                .ifPresent(ratingDto -> {
            Rating rating = new Rating();
            Optional.ofNullable(ratingDto.getRate()).ifPresent(rating::setRate);
            Optional.ofNullable(ratingDto.getCount()).ifPresent(rating::setCount);
            product.setRating(rating);
        });

        return product;
    }


    private FakeStoreProductRequestDto getFakeStoreProductRequestDto(Product product) {
        FakeStoreProductRequestDto fakeStoreProductRequestDto =
                new FakeStoreProductRequestDto();
        Optional.ofNullable(product.getName())
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
