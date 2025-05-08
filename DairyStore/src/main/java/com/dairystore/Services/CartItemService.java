package com.dairystore.Services;

import com.dairystore.Models.Cart;
import com.dairystore.Models.CartItem;
import com.dairystore.Models.Product;
import com.dairystore.Models.User;
import com.dairystore.Repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartItemService {
    private final CartItemRepository cartItemRepository;
    private final UserService userService;

    public void saveCartItems(Cart cart, Product product, int quantity) {
        Long cartId = cart.getId();
        Long productId = product.getId();
        if (cartItemRepository.existsByProductIdAndCartId(productId, cartId)) {
           updateCartItemQuantity(cart,product,quantity);
        } else {
          createCartItem(cart,product,quantity);
        }
    }

    private void updateCartItemQuantity(Cart cart, Product product, int quantity) {
        cartItemRepository.updateProductQuantity(product.getId(), quantity, cart.getId());
    }

    private void createCartItem(Cart cart, Product product, int quantity) {
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);
    }

    public void deleteCartItemsByProductId(long productId) throws Exception {
        User user = userService.getUserByUsername();
        Long cart_id = user.getCart().getId();
        if (!cartItemRepository.existsByProductIdAndCartId(productId, cart_id)) {
            throw new Exception("Продуктът не съществува в базата данни");
        }

        cartItemRepository.deleteByProductId(productId, cart_id);

    }

    public List<CartItem> getCartItemsByCart(Cart cart) {
        return cartItemRepository.findByCart(cart);
    }

    public void deleteCartItemByCartId(Long cartId) {
        cartItemRepository.deleteCartItemByCartId(cartId);
    }
}
