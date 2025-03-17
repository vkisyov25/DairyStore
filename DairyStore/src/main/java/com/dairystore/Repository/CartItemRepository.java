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
    @Query("DELETE FROM CartItem c WHERE c.product.id = :productId and c.cart.id = :cartId")
    void deleteByProductId(@Param("productId") Long productId,@Param("cartId") Long cartId);

    @Transactional
    @Modifying
    @Query("DELETE FROM CartItem cartItem WHERE cartItem.cart.id = ?1")
    void deleteCartItemByCartId(Long id);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM CartItem p WHERE p.product.id = :productId and p.cart.id = :cartId")
    boolean existsByProductIdAndCartId(@Param("productId") Long productId, @Param("cartId") Long cartId);


    @Modifying
    @Transactional
    @Query("UPDATE CartItem p SET p.quantity = p.quantity + :quantity WHERE p.product.id = :productId and p.cart.id=:cartId")
    void updateProductQuantity(@Param("productId") Long productId, @Param("quantity") int quantity, @Param("cartId") Long cartId);
}
