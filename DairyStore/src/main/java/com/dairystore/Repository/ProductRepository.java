package com.dairystore.Repository;

import com.dairystore.Models.Product;

import com.dairystore.Models.dtos.ViewProductAdminDto;
import com.dairystore.Models.dtos.ViewProductDto;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("SELECT new com.dairystore.Models.dtos.ViewProductDto(product.id, product.name, product.type, product.weight, product.price, product.description, product.discount, product.quantity) FROM Product product WHERE product.user.username =?1")
    List<ViewProductDto> findBySellerUsername(String username);

    @Query("SELECT new com.dairystore.Models.dtos.ViewProductAdminDto(product.id, product.name, product.type, product.weight, product.price, product.description, product.discount, product.quantity, product.user.username) FROM Product product")
    List<ViewProductAdminDto> findViewProductDtoList();

    @Query("SELECT new com.dairystore.Models.dtos.ViewProductDto(product.id, product.name, product.type, product.weight, product.price, product.description, product.discount, product.quantity) FROM Product product WHERE product.id =?1")
    ViewProductDto findByProductId(Long id);

    @Query("SELECT new com.dairystore.Models.dtos.ViewProductDto(product.id, product.name, product.type, product.weight, product.price, product.description, product.discount, product.quantity) FROM Product product")
    List<ViewProductDto> findProductsForSale();

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.quantity = p.quantity - :quantity WHERE p.id = :productId AND p.quantity >= :quantity")
    int updateProductQuantity(@Param("productId") Long productId, @Param("quantity") int quantity);

    boolean existsByName(String name);

    Product findProductByName(String name);

}