package com.example.redisdemo.service;

import com.example.redisdemo.model.Product;
import com.example.redisdemo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Cacheable(value = "products",key="#id")
    public Optional<Product> getProductById(Long id){
        System.out.println("Fetching product from database");
        return productRepository.findById(id);
    }

    @CachePut(value = "products",key="#product.id")
    public Product saveProduct(Product product){
        System.out.println("Saving product to database");
        return productRepository.save(product);
    }

    @CacheEvict(value = "products",key="#id")
    public void deleteProduct(Long id){
        System.out.println("Deleting product from database");
        productRepository.deleteById(id);
    }

    @CacheEvict(value = "products", allEntries = true)
    @Scheduled(fixedRateString = "${caching.productTTL}")
    public void scheduleDeleteProduct(){
        System.out.println("Deleting product from cache");
    }

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }
}
