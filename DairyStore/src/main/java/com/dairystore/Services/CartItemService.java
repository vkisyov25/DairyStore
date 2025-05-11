package com.dairystore.Services;

import com.dairystore.Models.Cart;
import com.dairystore.Models.CartItem;
import com.dairystore.Models.Product;

import java.util.List;

public interface CartItemService {
    void saveCartItems(Cart cart, Product product, int quantity);

    void deleteCartItemByProductId(long productId) throws Exception;

    List<CartItem> getCartItemsByCart(Cart cart);

    void deleteCartItemByCartId(Long cartId);
}
