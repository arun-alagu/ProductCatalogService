package com.example.productcatalogservice.controllers;

import com.example.productcatalogservice.dtos.*;
import com.example.productcatalogservice.models.Product;
import com.example.productcatalogservice.services.IProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {


    private final IProductService productService;

    public ProductController (IProductService productService) {
        this.productService = productService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProduct(@PathVariable("id") Long productId) {
            if (productId <= 0)
                throw new IllegalArgumentException("Invalid product id");
            Product product = productService.getProductById(productId);
            ProductResponseDto body = ProductResponseDto.getProductResponseDto(product);
            return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllProducts(){
        List<Product> products = productService.getAllProducts();
        List<ProductResponseDto> body = new LinkedList<>();
        for(Product product : products){
            body.add(ProductResponseDto.getProductResponseDto(product));
        }
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> addProduct(@RequestBody ProductRequestDto productRequestDto) {
        Product product = productService.addProduct(ProductRequestDto.getProduct(productRequestDto));
        ProductResponseDto body = ProductResponseDto.getProductResponseDto(product);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(@RequestBody ProductRequestDto productRequestDto,
            @PathVariable("id") Long productId){
        Product product = productService.updateProduct(ProductRequestDto.getProduct(productRequestDto)
                ,productId);
        ProductResponseDto body = ProductResponseDto.getProductResponseDto(product);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductResponseDto>  removeProduct(@PathVariable("id") Long productId){
        Product product = productService.deleteProduct(productId);
        ProductResponseDto body = ProductResponseDto.getProductResponseDto(product);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

}
