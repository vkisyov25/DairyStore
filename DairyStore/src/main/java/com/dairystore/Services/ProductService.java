package com.dairystore.Services;

import com.dairystore.Models.Product;
import com.dairystore.Models.dtos.CreateProductDto;
import com.dairystore.Models.dtos.ProductForSaleDto;
import com.dairystore.Models.dtos.ViewProductAdminDto;
import com.dairystore.Models.dtos.ViewProductDto;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface ProductService {
    List<ViewProductDto> getCurrentUserProducts();

    List<ViewProductAdminDto> getAllProducts();

    void createProduct(CreateProductDto createProductDto);

    List<Product> getProductsByType(String productType);

    Product getProductById(Long productId);

    void deleteById(Long id);

    ViewProductDto getById(Long id);

    void saveEditedProduct(ViewProductDto viewProductDto);

    List<ProductForSaleDto> getProductsForSale();

    void save(Product product);

    void isExist(String name, BindingResult bindingResult);
}
