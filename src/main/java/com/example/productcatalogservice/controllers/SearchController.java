package com.example.productcatalogservice.controllers;

import com.example.productcatalogservice.dtos.ProductResponseDto;
import com.example.productcatalogservice.models.Product;
import com.example.productcatalogservice.services.IProductSearchService;
import com.example.productcatalogservice.services.SelfProductSearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {
    private final IProductSearchService selfProductSearchService;

    SearchController(IProductSearchService selfProductSearchService) {
        this.selfProductSearchService = selfProductSearchService;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> search(
            @RequestParam("keyword") String keyword,
            @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "1") int pageSize
    ) {
        List<Product> resultProducts = selfProductSearchService.searchProducts(
                keyword, pageNumber, pageSize
        );
        
        List<ProductResponseDto> productResponseDtos = new ArrayList<>();
        for (Product product: resultProducts) {
            productResponseDtos.add(ProductResponseDto.getProductResponseDto(product));
        }
        
        return ResponseEntity.ok(productResponseDtos);
    }
}
