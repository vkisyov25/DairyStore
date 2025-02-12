package com.springSecurity.JWT.Services;

import com.springSecurity.JWT.Models.Cart;
import com.springSecurity.JWT.Models.CartItem;
import com.springSecurity.JWT.Models.Product;
import com.springSecurity.JWT.Models.User;
import com.springSecurity.JWT.Repository.CartItemRepository;
import com.springSecurity.JWT.Repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserService userService;
    private final ProductService productService;

    @Autowired
    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository, UserService userService, ProductService productService) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userService = userService;
        this.productService = productService;
    }

    public void addToCart(Long productId, int quantity) {
        User user = userService.getUserByUsername();
        Product product = productService.getProductById(productId);

        Cart cart = cartRepository.findByUser(user);
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
        }

        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);
    }

    public List<CartItem> getCartItems() {
        User user = userService.getUserByUsername();
        Cart cart = cartRepository.findByUser(user);
        return cartItemRepository.findByCart(cart);
    }

    public void deleteByProductId(long id) {
        cartItemRepository.deleteByProductId(id);
    }
}
