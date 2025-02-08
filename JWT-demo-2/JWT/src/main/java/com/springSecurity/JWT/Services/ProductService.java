package com.springSecurity.JWT.Services;

import com.springSecurity.JWT.Models.Product;
import com.springSecurity.JWT.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProductService {
    private ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProductByUsername(String username){
       return productRepository.findProductsByUsername(username);
    }
    public Product createProduct(Product product){
        return productRepository.save(product);
    }

    public List<Product> getProductsByType(String productType){
        return productRepository.findProductsByType(productType);
    }

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }
    public Product getProductById(Long productId) {
        return productRepository.findProductById(productId);
    }
}
