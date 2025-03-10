package com.example.productcatalogservice.controllers;

import com.example.productcatalogservice.dtos.ProductRequestDto;
import com.example.productcatalogservice.dtos.ProductResponseDto;

import com.example.productcatalogservice.exceptions.ProductNotFoundException;
import com.example.productcatalogservice.models.Category;
import com.example.productcatalogservice.models.Product;
import com.example.productcatalogservice.models.Rating;
import com.example.productcatalogservice.models.State;
import com.example.productcatalogservice.services.IProductService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class ProductControllerTest {

    /*~~(org/openrewrite/staticanalysis/LambdaBlockToExpression)~~>*/@BeforeAll
    static void setup() { // Load .env file before tests run
//        Dotenv dotenv = Dotenv.configure().load(); // This will load the .env file
//        System.setProperty("DB_URL", dotenv.get("DB_URL"));
//        System.setProperty("DB_NAME", dotenv.get("DB_NAME"));
//        System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
//        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
    }
    @Autowired
    private ProductController productController;
    @MockBean
    private IProductService productService;

    @Test
    void givenValidProductId_whenGetValidProduct_thenReturnProductAndResponse200() throws ProductNotFoundException {
        // Arrange
        Category category = new Category();
        category.setId(1L);
        category.setName("Test Category");
        category.setDescription("Test Category Description");

        Rating rating1 = new Rating();
        rating1.setId(1L);
        rating1.setRate(4.5f);

        Rating rating2 = new Rating();
        rating1.setId(2L);
        rating1.setRate(3.5f);

        Product product = new Product();
        product.setId(1L);
        product.setTitle("Test Product");
        product.setDescription("Test Product Description");
        product.getCategories().add(category);
        product.setState(State.ACTIVE);
//        product.setRating(new ArrayList<>(List.of(rating1, rating2)));

        category.setProducts((Set<Product>) List.of(product));
//        rating1.setProduct(product);
//        rating2.setProduct(product);

        when(productService.getProductById(1L)).thenReturn(product);

        // Act
        ResponseEntity<ProductResponseDto> response = productController.getProductById(1L);

        //Assert
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals("Test Product", response.getBody().getTitle());
        assertEquals("Test Product Description", response.getBody().getDescription());
        assertEquals(1L, response.getBody().getId());
//        Assertions.assertEquals(2, response.getBody().getRating().size());
        assertEquals("Test Category", response.getBody().getCategories().getFirst().getName());
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    void givenValidProductId_whenGetInvalidProduct_thenReturnResponse404() throws ProductNotFoundException {
        // Arrange
        when(productService.getProductById(1L)).thenReturn(null);

        ResponseEntity<ProductResponseDto> response = productController.getProductById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void givenInvalidProductId_whenGetValidProduct_thenThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> productController.getProductById(0L));
    }

    @Test
    void whenGetAllProducts_thenReturnListOfProductsAndResponse200() {
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
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(products.size(), response.getBody().size());
        assertEquals("test title1",response.getBody().getFirst().getTitle());
        assertEquals("test title2", response.getBody().get(1).getTitle());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void whenAddProduct_thenReturnAddedProductAndResponse201() throws ProductNotFoundException {
        // Arrange
        ProductRequestDto dto = new ProductRequestDto();
        dto.setTitle("test title");
        dto.setDescription("description");

        Product product = ProductRequestDto.getProduct(dto);

        Product newProduct = new Product();
        newProduct.setId(1L);
        newProduct.setTitle(product.getTitle());
        newProduct.setDescription(product.getDescription());

        when(productService.addProduct(any(Product.class))).thenReturn(newProduct);

        // Act
        ResponseEntity<ProductResponseDto> response = productController.addProduct(dto);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("test title", response.getBody().getTitle());
        assertEquals("description", response.getBody().getDescription());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void whenAddProductInvalidProductRequest_thenThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> productController.addProduct(null));
    }

    @Test
    void givenValidProductId_whenUpdateProduct_thenReturnUpdatedProductByIdAndResponse200() throws ProductNotFoundException {
        ProductRequestDto dto = new ProductRequestDto();
        dto.setTitle("test title");
        dto.setDescription("description");
        Product product = ProductRequestDto.getProduct(dto);
        product.setId(1L);

        Product updatedProduct = new Product();
        updatedProduct.setId(product.getId());
        updatedProduct.setTitle("new title");
        updatedProduct.setDescription(product.getDescription());

        when(productService.updateProductById(any(Product.class), eq(1L)))
                .thenReturn(updatedProduct);

        ResponseEntity<ProductResponseDto> response = productController.updateProductById(dto, 1L);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("new title", response.getBody().getTitle());
        assertEquals("description", response.getBody().getDescription());
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }


    @Test
    void givenValidProductIdAndInvalidProductRequest_whenUpdateProduct_thenThrowException() {

        assertThrows(IllegalArgumentException.class,
                () -> productController.updateProductById(null, 1L));
    }

    @Test
    void givenValidProductId_whenRemoveProduct_thenReturnProductByIdAndResponse200() throws ProductNotFoundException {
        // Arrange
        Product product = new Product();
        product.setId(1L);
        product.setTitle("test title");
        product.setDescription("description");

        Product deletedProduct = product;
        product = null;
        when(productService.deleteProductById(1L)).thenReturn(deletedProduct);

        // Act
        ResponseEntity<ProductResponseDto> response = productController.removeProductById(1L);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("test title", response.getBody().getTitle());
        assertEquals("description", response.getBody().getDescription());
        assertEquals(1L, response.getBody().getId());
        assertEquals(ProductResponseDto.class, response.getBody().getClass());

    }




}