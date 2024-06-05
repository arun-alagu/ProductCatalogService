package com.example.productcatalogservice.clients;

import com.example.productcatalogservice.dtos.FakeStoreProductRequestDto;
import com.example.productcatalogservice.dtos.FakeStoreProductResponseDto;
import com.example.productcatalogservice.models.Product;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class FakeStoreClient {

    private final RestTemplate restTemplate;

    public FakeStoreClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public FakeStoreProductResponseDto getProduct(Long productId){
        return responseForEntity(
                HttpMethod.GET, "https://fakestoreapi.com/products/{id}" ,
                null, FakeStoreProductResponseDto.class, productId).getBody();
    }

    public FakeStoreProductResponseDto[] getAllProducts(){
        return responseForEntity(
                HttpMethod.GET, "https://fakestoreapi.com/products" ,
                null, FakeStoreProductResponseDto[].class).getBody();
    }

    public FakeStoreProductResponseDto addProduct(
            FakeStoreProductRequestDto fakeStoreProductRequestDto){
        return responseForEntity(
                HttpMethod.POST, "https://fakestoreapi.com/products",
                fakeStoreProductRequestDto, FakeStoreProductResponseDto.class).getBody();
    }

    public FakeStoreProductResponseDto updateProduct(
            FakeStoreProductRequestDto fakeStoreProductRequestDto,
            Long productId){
        return responseForEntity(
                HttpMethod.PUT, "https://fakestoreapi.com/products/{id}",
                fakeStoreProductRequestDto, FakeStoreProductResponseDto.class,
                productId).getBody();
    }

    public FakeStoreProductResponseDto deleteProduct(Long productId){
        return responseForEntity(
                HttpMethod.DELETE, "https://fakestoreapi.com/products/{id}",
                null, FakeStoreProductResponseDto.class, productId).getBody();
    }


    private <T> ResponseEntity<T> responseForEntity(
            HttpMethod httpMethod, String url,
            @Nullable Object request, Class<T> responseType,
            Object... uriVariables) throws RestClientException {
        RequestCallback requestCallback = restTemplate.httpEntityCallback(request, responseType);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = restTemplate.
                responseEntityExtractor(responseType);
        return restTemplate.execute(url, httpMethod, requestCallback,
                responseExtractor, uriVariables);
    }


}
