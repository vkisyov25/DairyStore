package com.dairystore.Repository;

import com.dairystore.Models.Cart;
import com.dairystore.Models.CartItem;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCart(Cart cart);

    @Transactional
    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.product.id = :productId")
    void deleteByProductId(@Param("productId") Long productId);

    @Transactional
    @Modifying
    @Query("DELETE FROM CartItem cartItem WHERE cartItem.cart.id = ?1")
    void deleteCartItemByCartId(Long id);
}
