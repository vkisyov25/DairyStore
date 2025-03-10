package com.springSecurity.JWT.Services;

import com.springSecurity.JWT.Models.Product;
import com.springSecurity.JWT.Models.User;
import com.springSecurity.JWT.Models.dtos.CreateProductDto;
import com.springSecurity.JWT.Models.dtos.SellerViewProductDto;
import com.springSecurity.JWT.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final UserService userService;

    public List<SellerViewProductDto> getCurrentUserProducts() {
        User user = userService.getUserByUsername();
        return productRepository.findBySellerUsername(user.getUsername());
    }

    public void createProduct(CreateProductDto createProductDto) {
        User user = userService.getUserByUsername();
        Product product = Product.builder()
                .name(createProductDto.getName())
                .type(createProductDto.getType())
                .weight(createProductDto.getWeight())
                .price(createProductDto.getPrice())
                .description(createProductDto.getDescription())
                .discount(createProductDto.getDiscount())
                .quantity(createProductDto.getQuantity())
                .user(user).build();

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