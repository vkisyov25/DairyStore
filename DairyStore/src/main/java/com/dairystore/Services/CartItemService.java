package com.dairystore.Services;

import com.dairystore.Models.Cart;
import com.dairystore.Models.CartItem;
import com.dairystore.Models.Product;
import com.dairystore.Repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartItemService {
    private final CartItemRepository cartItemRepository;

    public void saveCartItems(Cart cart, Product product, int quantity, double totalPrice) {
        if (cartItemRepository.existsByProductId(product.getId())) {
            cartItemRepository.updateProductQuantity(product.getId(), quantity);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setTotalPrice(totalPrice);
            cartItemRepository.save(cartItem);
        }
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
