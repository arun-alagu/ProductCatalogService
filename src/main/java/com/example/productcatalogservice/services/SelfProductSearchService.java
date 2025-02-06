package com.example.productcatalogservice.services;

import com.example.productcatalogservice.models.Product;
import com.example.productcatalogservice.repositories.ProductRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SelfProductSearchService implements IProductSearchService {
    private final ProductRepository productRepository;

    SelfProductSearchService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> searchProducts(String titleSearchText, int pageNumber, int pageSize) {
        return productRepository.findByTitleContains(titleSearchText,
                PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.ASC, "price")));
    }
}
