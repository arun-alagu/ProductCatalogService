package com.example.productcatalogservice.controllers;

import com.example.productcatalogservice.dtos.*;
import com.example.productcatalogservice.models.Category;
import com.example.productcatalogservice.models.Product;
import com.example.productcatalogservice.models.Rating;
import com.example.productcatalogservice.services.IProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {


    private final IProductService productService;

    public ProductController(IProductService productService) {
        this.productService = productService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProduct(@PathVariable("id") Long productId) {
            if (productId <= 0)
                throw new IllegalArgumentException("Invalid product id");
            Product product = productService.getProductById(productId);
            ProductResponseDto body = getProductResponseDto(product);
            return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllProducts(){
        List<Product> products = productService.getAllProducts();
        List<ProductResponseDto> body = new LinkedList<>();
        for(Product product : products){
            body.add(getProductResponseDto(product));
        }
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> addProduct(@RequestBody ProductRequestDto productRequestDto) {
        Product product = productService.addProduct(getProduct(productRequestDto));
        ProductResponseDto body = getProductResponseDto(product);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(@RequestBody ProductRequestDto productRequestDto,
            @PathVariable("id") Long productId){
        Product product = productService.updateProduct(getProduct(productRequestDto) ,productId);
        ProductResponseDto body = getProductResponseDto(product);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductResponseDto>  removeProduct(@PathVariable("id") Long productId){
        Product product = productService.deleteProduct(productId);
        ProductResponseDto body = getProductResponseDto(product);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    private ProductResponseDto getProductResponseDto(Product product){
        ProductResponseDto productResponseDto = new ProductResponseDto();

        Optional.ofNullable(product.getId()).ifPresent(productResponseDto::setId);
        Optional.ofNullable(product.getName()).ifPresent(productResponseDto::setName);
        Optional.ofNullable(product.getDescription()).ifPresent(productResponseDto::setDescription);
        Optional.ofNullable(product.getPrice()).ifPresent(productResponseDto::setPrice);
        Optional.ofNullable(product.getImageUrl()).ifPresent(productResponseDto::setImageUrl);

        Optional.ofNullable(product.getCategory()).ifPresent(category -> {
            CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
            Optional.ofNullable(category.getId()).ifPresent(categoryResponseDto::setId);
            Optional.ofNullable(category.getName()).ifPresent(categoryResponseDto::setName);
            productResponseDto.setCategory(categoryResponseDto);
        });

        Optional.ofNullable(product.getRating()).ifPresent(ratings -> {
            List<RatingResponseDto> ratingResponseDtoList = new LinkedList<>();
            for(Rating rating : ratings){
                RatingResponseDto ratingResponseDto = new RatingResponseDto();
                Optional.ofNullable(rating.getRate()).ifPresent(ratingResponseDto::setRate);
                ratingResponseDtoList.add(ratingResponseDto);
            }
            productResponseDto.setRating(ratingResponseDtoList);
        });

        return productResponseDto;
    }

    private Product getProduct(ProductRequestDto productRequestDto){
        Product product = new Product();

        Optional.ofNullable(productRequestDto.getName()).ifPresent(product::setName);
        Optional.ofNullable(productRequestDto.getPrice()).ifPresent(product::setPrice);
        Optional.ofNullable(productRequestDto.getDescription()).ifPresent(product::setDescription);
        Optional.ofNullable(productRequestDto.getImageUrl()).ifPresent(product::setImageUrl);

        Optional.ofNullable(productRequestDto.getCategory()).ifPresent(categoryRequestDto -> {
            Category category = new Category();
            Optional.ofNullable(categoryRequestDto.getName()).ifPresent(category::setName);
            Optional.ofNullable(categoryRequestDto.getDescription()).ifPresent(category::setDescription);
            product.setCategory(category);
        });

        return product;
    }
}
