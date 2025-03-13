package com.dairystore.Repository;

import com.dairystore.Models.Product;
import com.dairystore.Models.dtos.SellerViewProductDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("select product from Product product where product.user.username = ?1")
    List<Product> findProductsByUsername(String username);

    @Query("select product from Product product where product.type = ?1")
    List<Product> findProductsByType(String type);

    @Query("select product from Product product where product.id = ?1")
    Product findProductById(Long id);

    @Query("SELECT new com.springSecurity.JWT.Models.dtos.SellerViewProductDto(product.id, product.name, product.type, product.weight, product.price, product.description, product.discount, product.quantity) FROM Product product WHERE product.user.username =?1")
    List<SellerViewProductDto> findBySellerUsername(String username);

    @Query("SELECT new com.springSecurity.JWT.Models.dtos.SellerViewProductDto(product.id, product.name, product.type, product.weight, product.price, product.description, product.discount, product.quantity) FROM Product product WHERE product.id =?1")
    SellerViewProductDto findByProductId(Long id);
}
