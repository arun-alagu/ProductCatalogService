package com.example.productcatalogservice.controllers;

import com.example.productcatalogservice.dtos.ProductResponseDto;
import com.example.productcatalogservice.models.Product;
import com.example.productcatalogservice.services.IProductSearchService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class SearchControllerTest {
    @BeforeAll
    static void setup() { // Load .env file before tests run
//        Dotenv dotenv = Dotenv.configure().load(); // This will load the .env file
//        System.setProperty("DB_URL", dotenv.get("DB_URL"));
//        System.setProperty("DB_NAME", dotenv.get("DB_NAME"));
//        System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
//        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
    }

    @Autowired
    private SearchController searchController;

    @Mock
    private RestTemplate restTemplate;

    @MockBean
    private IProductSearchService productSearchService;

    @Test
    void whenValidProductSearchRequest_thenReturnProducts() {
        Product product1 = new Product();
        product1.setId(1L);
        product1.setTitle("Product 1");

        Product product2 = new Product();
        product2.setId(2L);
        product2.setTitle("Product 2");

        List<Product> products = List.of(product1, product2);
        when(productSearchService.searchProducts("Product", 0, 1))
        .thenReturn(products);

        ResponseEntity<List<ProductResponseDto>> productResponseDtos = searchController.search(
                "Product", 0, 1);

        assertNotNull(products);
        assertNotNull(productResponseDtos.getBody());
        assertEquals(products.size(), productResponseDtos.getBody().size());
        assertEquals(2, products.size());
        assertEquals(products.getFirst().getTitle(),
                productResponseDtos.getBody().getFirst().getTitle());

    }

    @Test
    void whenGETSearchRequest_thenReturnProducts() {
        Product product1 = new Product();
        product1.setId(1L);
        product1.setTitle("Product 1");
        Product product2 = new Product();
        product2.setId(2L);
        product2.setTitle("Product 2");
        List<Product> products = List.of(product1, product2);
        when(productSearchService.searchProducts("Product", 0, 1))
        .thenReturn(products);

        ResponseEntity<List<ProductResponseDto>> productResponseDtos = searchController.search(
                "Product", 0, 1
        );
       when(restTemplate.getForEntity("http://localhost:8080/search?keyword=Product",
               ProductResponseDtoList.class))
               .thenReturn(new ResponseEntity<>(
                       new ProductResponseDtoList(
                              productResponseDtos
                               .getBody()), HttpStatus.OK)
               );

        ResponseEntity<ProductResponseDtoList> productResponseDtoList =
        restTemplate.getForEntity("http://localhost:8080/search?keyword=Product",
                ProductResponseDtoList.class);

        assertNotNull(productResponseDtoList.getBody());
        assertEquals(products.size(), productResponseDtoList.getBody().size());
        assertEquals(products.getFirst().getTitle(),
                productResponseDtoList.getBody().getFirst().getTitle());


    }

    static class ProductResponseDtoList extends ArrayList<ProductResponseDto> {

        public ProductResponseDtoList(List<ProductResponseDto> body) {
            this.addAll(body);
        }
    }
}