package com.example.productcatalogservice.controllers;

import com.example.productcatalogservice.dtos.ProductRequestDto;
import com.example.productcatalogservice.dtos.ProductResponseDto;
import com.example.productcatalogservice.exceptions.ProductNotFoundException;
import com.example.productcatalogservice.models.Product;
import com.example.productcatalogservice.services.IProductService;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.mockito.Mockito.when;

@SpringBootTest
class ProductControllerTest {

    // Load .env file before tests run
    @BeforeAll
    public static void setup() {
        Dotenv dotenv = Dotenv.load(); // This will load the .env file
        System.setProperty("DB_URL", dotenv.get("DB_URL"));
        System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
    }

    @Autowired
    private ProductController productController;
    @MockBean
    private IProductService productService;

    @Test
    void whenGetProductById_thenReturnProduct() throws ProductNotFoundException {
        // Arrange
        Product product = new Product();
        product.setId(1L);
        product.setTitle("test title");
        product.setDescription("description");

        when(productService.getProductById(1L)).thenReturn(product);

        // Act
        ResponseEntity<ProductResponseDto> response = productController.getProduct(1L);

        //Assert
        Assertions.assertEquals("test title",
                Objects.requireNonNull(response.getBody()).getTitle());

    }

    @Test
    void getAllProducts() {
        // Arrange
        Product product1 = new Product();
        product1.setId(1L);
        product1.setTitle("test title1");

        Product product2 = new Product();
        product2.setId(2L);
        product2.setTitle("test title2");

        List<Product> products = new ArrayList<>(List.of(product1, product2));
        when(productService.getAllProducts()).thenReturn(products);

        // Act
        ResponseEntity<List<ProductResponseDto>> response = productController.getAllProducts();

        // Assert
        Assertions.assertEquals(products.size(),
                Objects.requireNonNull(response.getBody()).size());
        Assertions.assertEquals("test title1",
                Objects.requireNonNull(response.getBody()).get(0).getTitle());
        Assertions.assertEquals("test title2",
                Objects.requireNonNull(response.getBody()).get(1).getTitle());
    }

    @Test
    void addProduct() {
        // Arrange

        ProductRequestDto prdto = new ProductRequestDto();
        prdto.setTitle("test title");
        prdto.setDescription("description");

        Product product = ProductRequestDto.getProduct(prdto);

        Product newProduct = new Product();
        newProduct.setId(1L);
        newProduct.setTitle("test title");
        newProduct.setDescription("description");

        when(productService.addProduct(product))
                .thenReturn(newProduct);

        // Act
        ResponseEntity<ProductResponseDto> response = productController.addProduct(prdto);

        // Assert
        Assertions.assertEquals(1L,
                Objects.requireNonNull(response.getBody()).getId());
        Assertions.assertEquals("test",
                Objects.requireNonNull(response.getBody()).getTitle());
    }

    @Test
    void updateProduct() {
    }

    @Test
    void removeProduct() {
    }
}