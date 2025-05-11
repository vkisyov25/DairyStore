package com.dairystore.Services;

import com.dairystore.Models.Product;
import com.dairystore.Models.dtos.CreateProductDto;
import com.dairystore.Models.dtos.ProductForSaleDto;
import com.dairystore.Models.dtos.ViewProductDto;

import java.util.List;

public interface ProductService {
    List<ViewProductDto> getCurrentUserProducts();

    void createProduct(CreateProductDto createProductDto);

    List<Product> getProductsByType(String productType);

    Product getProductById(Long productId);

    void deleteById(Long id);

    ViewProductDto getById(Long id);

    void saveEditedProduct(ViewProductDto viewProductDto);

    List<ProductForSaleDto> getProductsForSale();

    void save(Product product);
}
