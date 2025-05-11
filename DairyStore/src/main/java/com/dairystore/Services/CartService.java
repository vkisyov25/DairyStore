package com.dairystore.Services;

import com.dairystore.Models.Cart;
import com.dairystore.Models.User;
import com.dairystore.Models.dtos.ShoppingCartDto;

import java.util.List;

public interface CartService {
    void addToCart(Long productId, int quantity) throws Exception;

    List<ShoppingCartDto> viewShoppingCart();

    Cart getCartByUser(User user);
}
