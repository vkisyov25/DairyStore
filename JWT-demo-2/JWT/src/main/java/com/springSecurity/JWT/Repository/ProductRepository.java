package com.springSecurity.JWT.Repository;

import com.springSecurity.JWT.Models.Cart;
import com.springSecurity.JWT.Models.Product;
import com.springSecurity.JWT.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    @Query("select product from Product product where product.user.username = ?1")
    List<Product> findProductsByUsername (String username);

    @Query("select product from Product product where product.type = ?1")
    List<Product> findProductsByType (String username);

    @Query("select product from Product product where product.id = ?1")
    Product findProductById (Long id);


}
