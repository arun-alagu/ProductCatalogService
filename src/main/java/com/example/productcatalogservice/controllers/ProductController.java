package com.example.productcatalogservice.controllers;

import com.example.productcatalogservice.dtos.*;
import com.example.productcatalogservice.exceptions.ProductNotFoundException;
import com.example.productcatalogservice.models.Product;
import com.example.productcatalogservice.services.IProductService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final IProductService productService;

    public ProductController (IProductService productService) {
        this.productService = productService;
    }
//    public ProductController (@Qualifier("FakeStoreProductService") IProductService productService) {
//        this.productService = productService;
//    }


    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable("id") Long productId) throws ProductNotFoundException {
            if (productId <= 0)
                throw new IllegalArgumentException("Invalid product id");
            Product product = productService.getProductById(productId);
            if (product == null)
                return ResponseEntity.notFound().build();
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
    public ResponseEntity<ProductResponseDto> addProduct(@RequestBody ProductRequestDto productRequestDto) throws ProductNotFoundException {
        if (productRequestDto == null)
            throw new IllegalArgumentException("Invalid product request");
        Product newProduct = ProductRequestDto.getProduct(productRequestDto);
        productService.addProduct(newProduct);
        Product addedProduct = productService.addProduct(newProduct);

        if (addedProduct == null) throw new RuntimeException("Failed to add product");
        ProductResponseDto body = ProductResponseDto.getProductResponseDto(addedProduct);
        return new ResponseEntity<>(body, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProductById(@RequestBody ProductRequestDto productRequestDto,
                                                                @PathVariable("id") Long productId) throws ProductNotFoundException {
        if(productRequestDto == null)
            throw new IllegalArgumentException("Invalid product request");
        Product product = productService.updateProductById(
                ProductRequestDto.getProduct(productRequestDto)
                ,productId);
        if (product == null) return ResponseEntity.notFound().build();
        ProductResponseDto body = ProductResponseDto.getProductResponseDto(product);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductResponseDto> removeProductById(@PathVariable("id") Long productId) throws ProductNotFoundException {
        Product product = productService.deleteProductById(productId);
        ProductResponseDto body = ProductResponseDto.getProductResponseDto(product);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

}
