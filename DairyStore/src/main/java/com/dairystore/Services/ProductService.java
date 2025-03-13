package com.dairystore.Services;

import com.dairystore.Models.Product;
import com.dairystore.Models.User;
import com.dairystore.Models.dtos.CreateProductDto;
import com.dairystore.Models.dtos.SellerViewProductDto;
import com.dairystore.Repository.ProductRepository;
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

    public void deleteById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Продуктът с ID " + id + " не съществува!");
        }
        productRepository.deleteById(id);
    }

    public SellerViewProductDto getById(Long id) {
        return productRepository.findByProductId(id);
    }

    public void saveEditedProduct(SellerViewProductDto sellerViewProductDto) {
        Product product = productRepository.findProductById(sellerViewProductDto.getId());

        product.setId(sellerViewProductDto.getId());
        product.setName(sellerViewProductDto.getName());
        product.setType(sellerViewProductDto.getType());
        product.setWeight(sellerViewProductDto.getWeight());
        product.setPrice(sellerViewProductDto.getPrice());
        product.setDescription(sellerViewProductDto.getDescription());
        product.setDiscount(sellerViewProductDto.getDiscount());
        product.setQuantity(sellerViewProductDto.getQuantity());

        productRepository.save(product);
    }
}