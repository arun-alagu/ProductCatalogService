package com.example.productcatalogservice.services;

import com.example.productcatalogservice.exceptions.ProductNotFoundException;
import com.example.productcatalogservice.exceptions.RatingNotFoundException;
import com.example.productcatalogservice.models.Product;
import com.example.productcatalogservice.models.Rating;
import com.example.productcatalogservice.repositories.ProductRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service(value = "SelfProductService")
@Primary
public class SelfProductService implements  IProductService {

    private final ProductRepository productRepository;
    private final IRatingService ratingService;
    private final RedisTemplate<String, Object> redisTemplateObject;
    private final RedisTemplate<String, List<Object>> redisTemplateObjects;

    public SelfProductService(ProductRepository productRepository, 
    		RedisTemplate<String, Object> redisTemplateObject, 
    		RedisTemplate<String, List<Object>> redisTemplateObjects,
    		IRatingService ratingService) {
        this.productRepository = productRepository;
		this.ratingService = ratingService;
        this.redisTemplateObject = redisTemplateObject;
        this.redisTemplateObjects = redisTemplateObjects;
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> products = (List<Product>) redisTemplateObjects.opsForHash().get("PRODUCTS", "PRODUCTS_");
        if (products != null && !products.isEmpty() && redisTemplateObjects.getExpire("PRODUCTS") > System.currentTimeMillis()) return products;
//        if (products != null && !products.isEmpty()) return products;
        products = productRepository.findAll();
        redisTemplateObjects.expire("PRODUCTS", 30, TimeUnit.SECONDS);
        redisTemplateObjects.opsForHash().put("PRODUCTS", "PRODUCTS_", products);
        return products;
//        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long productId) throws ProductNotFoundException {
//        return productRepository.findById(productId)
//                .orElseThrow(()-> new ProductNotFoundException(productId ,"Product not found"));
        Product product = (Product) redisTemplateObject.opsForHash().get("PRODUCT", "PRODUCT_" +productId);
        if (product != null) return product;

        product = productRepository.findById(productId)
                .orElseThrow(()-> new ProductNotFoundException("Product with"+productId+" not found"));
        redisTemplateObject.expire("PRODUCT", 30, TimeUnit.SECONDS);
        redisTemplateObject.opsForHash().put("PRODUCT", "PRODUCT_" + productId, product);
        return product;
    }

    @Override
    public Product addProduct(Product product) throws ProductNotFoundException {
        if(product == null) throw new IllegalArgumentException("Product cannot be null");
        if(product.getRating() == null) product.setRating(new Rating());
        try {
        ratingService.addRating(product.getRating());
        } catch (RatingNotFoundException e) {
        	throw new ProductNotFoundException(e.getMessage()+", Product not added");
        }
        return Optional.of(productRepository.save(product)).orElseThrow(
                ()-> new ProductNotFoundException("Product not added")
        );
    }

    @Override
    public Product updateProductById(Product product, Long productId) throws ProductNotFoundException {
        if(product == null) throw new IllegalArgumentException("Product cannot be null");
        Product updatedProduct = getProductById(productId);

        Optional.ofNullable(product.getTitle()).ifPresent(updatedProduct::setTitle);
        Optional.ofNullable(product.getDescription()).ifPresent(updatedProduct::setDescription);
        Optional.ofNullable(product.getPrice()).ifPresent(updatedProduct::setPrice);
        Optional.ofNullable(product.getImageUrl()).ifPresent(updatedProduct::setImageUrl);
        Optional.ofNullable(product.getCategories()).ifPresent(categories -> {
        	categories.forEach(updatedProduct.getCategories()::add);
        });
        Optional.ofNullable(product.getRating()).ifPresent(updatedProduct::setRating);
        
        return addProduct(updatedProduct);
//        return Optional.of(productRepository.save(updatedProduct)).orElseThrow(
//                ()-> new ProductNotFoundException("Product not found")
//        );
    }

    @Override
    public Product deleteProductById(Long productId) throws ProductNotFoundException {
        Product deletedProduct = getProductById(productId);
        productRepository.deleteById(productId);
        return deletedProduct;
    }

	@Override
	public List<Product> addProducts(List<Product> products) throws ProductNotFoundException {
		List<Product> addedProducts = new ArrayList<>();
		for(Product product : products) {
			addedProducts.add(addProduct(product));
		}
		return addedProducts;
	}
}
