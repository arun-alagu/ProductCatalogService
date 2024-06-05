package com.example.productcatalogservice.services;

import com.example.productcatalogservice.dtos.*;
import com.example.productcatalogservice.models.Category;
import com.example.productcatalogservice.models.Product;
import com.example.productcatalogservice.models.Rating;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.nonNull;

@Service
public class FakeStoreProductService implements IFakeStoreProductService{

    private final RestTemplateBuilder restTemplateBuilder;

    public FakeStoreProductService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplateBuilder = restTemplateBuilder;
    }

    @Override
    public List<Product> getAllProducts() {
        RestTemplate restTemplate = restTemplateBuilder.build();

        FakeStoreProductResponseDto[] fakeStoreProductResponseDtos = restTemplate.getForEntity(
                "https://fakestoreapi.com/products" , FakeStoreProductResponseDto[].class).getBody();


        List<Product> products = new LinkedList<>();
        if(fakeStoreProductResponseDtos != null) {
            for (FakeStoreProductResponseDto fakeStoreProductResponseDto : fakeStoreProductResponseDtos) {
                products.add(getProduct(fakeStoreProductResponseDto));
            }
        }
        return products;
    }

    @Override
    public Product getProductById(Long productId) {
        RestTemplate restTemplate = restTemplateBuilder.build();

        FakeStoreProductResponseDto fakeStoreProductResponseDto = restTemplate.getForEntity(
                "https://fakestoreapi.com/products/{id}", FakeStoreProductResponseDto.class,productId).getBody();
        if(fakeStoreProductResponseDto != null)
            return getProduct(fakeStoreProductResponseDto);
        return null;
    }

    @Override
    public Product addProduct(Product product) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        FakeStoreProductResponseDto fakeStoreProductResponseDto = restTemplate.postForEntity(
                "https://fakestoreapi.com/products", getFakeStoreProductRequest(product), FakeStoreProductResponseDto.class).getBody();
        if(fakeStoreProductResponseDto != null)
            return getProduct(fakeStoreProductResponseDto);
        return null;
    }

    @Override
    public Product updateProduct(Product product, Long productId) {
        FakeStoreProductResponseDto fakeStoreProductResponseDto = responseForEntity(
                HttpMethod.PUT, "https://fakestoreapi.com/products/{id}",
                getFakeStoreProductRequest(product), FakeStoreProductResponseDto.class, productId).getBody();
        if(fakeStoreProductResponseDto != null)
            return getProduct(fakeStoreProductResponseDto);
        return null;
    }

    @Override
    public Product deleteProduct(Long productId) {
        FakeStoreProductResponseDto fakeStoreProductResponseDto = responseForEntity(
                HttpMethod.DELETE, "https://fakestoreapi.com/products/{id}", null, FakeStoreProductResponseDto.class, productId).getBody();
        if(fakeStoreProductResponseDto != null)
            return getProduct(fakeStoreProductResponseDto);
        return null;
    }


    private <T> ResponseEntity<T> responseForEntity(HttpMethod httpMethod, String url, @Nullable Object request, Class<T> responseType, Object... uriVariables) throws RestClientException {
        RestTemplate restTemplate = restTemplateBuilder.build();
        RequestCallback requestCallback = restTemplate.httpEntityCallback(request, responseType);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = restTemplate.responseEntityExtractor(responseType);
        return restTemplate.execute(url, httpMethod, requestCallback, responseExtractor, uriVariables);
    }
    private Product getProduct(FakeStoreProductResponseDto fakeStoreProductResponseDto) {
        Product product = new Product();
        Optional.ofNullable(fakeStoreProductResponseDto.getId()).ifPresent(product::setId);
        Optional.ofNullable(fakeStoreProductResponseDto.getTitle()).ifPresent(product::setName);
        Optional.ofNullable(fakeStoreProductResponseDto.getPrice()).ifPresent(product::setPrice);
        Optional.ofNullable(fakeStoreProductResponseDto.getDescription()).ifPresent(product::setDescription);
        Optional.ofNullable(fakeStoreProductResponseDto.getImage()).ifPresent(product::setImageUrl);

        Optional.ofNullable(fakeStoreProductResponseDto.getCategory()).ifPresent(categoryName -> {
            Category category = new Category();
            category.setName(categoryName);
            product.setCategory(category);
        });

        Optional.ofNullable(fakeStoreProductResponseDto.getRating()).ifPresent(ratingDto -> {
            Rating rating = new Rating();
            Optional.ofNullable(ratingDto.getRate()).ifPresent(rating::setRate);
            Optional.ofNullable(ratingDto.getCount()).ifPresent(rating::setCount);
            product.setRating(rating);
        });

        return product;
    }


    private FakeStoreProductRequestDto getFakeStoreProductRequest(Product product) {
        FakeStoreProductRequestDto fakeStoreProductRequestDto = new FakeStoreProductRequestDto();
        Optional.ofNullable(product.getName()).ifPresent(fakeStoreProductRequestDto::setTitle);
        Optional.ofNullable(product.getDescription()).ifPresent(fakeStoreProductRequestDto::setDescription);
        Optional.ofNullable(product.getPrice()).ifPresent(fakeStoreProductRequestDto::setPrice);
        Optional.ofNullable(product.getImageUrl()).ifPresent(fakeStoreProductRequestDto::setImage);

        Optional.ofNullable(product.getCategory()).flatMap(
                category -> Optional.ofNullable(category.getName()))
                .ifPresent(fakeStoreProductRequestDto::setCategory);

        return fakeStoreProductRequestDto;
    }



}
