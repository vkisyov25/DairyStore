package com.dairystore.Services;

import com.dairystore.Models.Product;
import com.dairystore.Models.User;
import com.dairystore.Models.dtos.CreateProductDto;
import com.dairystore.Models.dtos.ProductForSaleDto;
import com.dairystore.Models.dtos.ViewProductDto;
import com.dairystore.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final UserService userService;

    public List<ViewProductDto> getCurrentUserProducts() {
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

    public ViewProductDto getById(Long id) {
        return productRepository.findByProductId(id);
    }

    public void saveEditedProduct(ViewProductDto viewProductDto) {
        Product product = productRepository.findProductById(viewProductDto.getId());

        product.setId(viewProductDto.getId());
        product.setName(viewProductDto.getName());
        product.setType(viewProductDto.getType());
        product.setWeight(viewProductDto.getWeight());
        product.setPrice(viewProductDto.getPrice());
        product.setDescription(viewProductDto.getDescription());
        product.setDiscount(viewProductDto.getDiscount());
        product.setQuantity(viewProductDto.getQuantity());

        productRepository.save(product);
    }

    public List<ProductForSaleDto> getProductsForSale() {
        List<ViewProductDto> productsForSale = productRepository.findProductsForSale();
        List<ProductForSaleDto> productForSaleDtoList = new ArrayList<>();
        for (int i = 0; i < productsForSale.size(); i++) {
            int quantity = productsForSale.get(i).getQuantity();
            String availability = "";
            if (quantity >= 1) {
                availability = "В наличност";
            } else {
                availability = "Изчерпано";
            }

            ProductForSaleDto productForSaleDto = ProductForSaleDto.builder()
                    .id(productsForSale.get(i).getId())
                    .name(productsForSale.get(i).getName())
                    .type(productsForSale.get(i).getType())
                    .weight(productsForSale.get(i).getWeight())
                    .price(productsForSale.get(i).getPrice())
                    .description(productsForSale.get(i).getDescription())
                    .availability(availability)
                    .build();

            productForSaleDtoList.add(productForSaleDto);
        }

        return productForSaleDtoList;
    }

    public void save(Product product) {
        productRepository.save(product);
    }
}