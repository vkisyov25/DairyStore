package com.springSecurity.JWT.Services;

import com.springSecurity.JWT.Models.Product;
import com.springSecurity.JWT.Models.User;
import com.springSecurity.JWT.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final UserService userService;

    @Autowired
    public ProductService(ProductRepository productRepository, UserService userService) {
        this.productRepository = productRepository;
        this.userService = userService;
    }

    public List<Product> getAllProductByUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            return productRepository.findProductsByUsername(username);
        }
        return new ArrayList<>();
    }

    public void createProduct(Product product) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("User is not authenticated");
        }
        User user = userService.getUserByUsername(authentication.getName());
        product.setUser(user);
        productRepository.save(product);
    }

    public List<Product> getProductsByType(String productType) {
        if (productType.equals("всички")) {
            return productRepository.findAll();
        } else {
            return productRepository.findProductsByType(productType);
        }
    }

    public Product getProductById(Long productId) {
        return productRepository.findProductById(productId);
    }
}
