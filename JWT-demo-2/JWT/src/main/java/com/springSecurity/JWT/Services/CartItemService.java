package com.springSecurity.JWT.Services;

import com.springSecurity.JWT.Models.Cart;
import com.springSecurity.JWT.Models.CartItem;
import com.springSecurity.JWT.Models.Product;
import com.springSecurity.JWT.Repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartItemService {
    private final CartItemRepository cartItemRepository;

    public void saveCartItems(Cart cart, Product product, int quantity, double totalPrice) {
        //Създава и добавя в базата данни CartItem
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        cartItem.setTotalPrice(totalPrice);
        cartItemRepository.save(cartItem);
    }

    public void deleteCartItemsByProductId(long id) {
        cartItemRepository.deleteByProductId(id);
    }

    public List<CartItem> getCartItemsByCart(Cart cart) {
        return cartItemRepository.findByCart(cart);
    }

    public void deleteCartItemByCartId(Long cartId) {
        cartItemRepository.deleteCartItemByCartId(cartId);
    }
}
