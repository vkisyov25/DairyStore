package com.springSecurity.JWT.Services;

import com.springSecurity.JWT.Models.Cart;
import com.springSecurity.JWT.Models.CartItem;
import com.springSecurity.JWT.Models.Product;
import com.springSecurity.JWT.Models.User;
import com.springSecurity.JWT.Repository.CartItemRepository;
import com.springSecurity.JWT.Repository.CartRepository;
import com.springSecurity.JWT.Repository.ProductRepository;
import com.springSecurity.JWT.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final ProductService productService;

    @Autowired
    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository,
                       ProductRepository productRepository, UserRepository userRepository, UserService userService, ProductService productService) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.productService = productService;
    }

    public void addToCart(Long productId, int quantity) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByUsername(authentication.getName());
        Long userId = user.getId();
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

    public List<CartItem> getCartItems(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        Cart cart = cartRepository.findByUser(user);
        return cartItemRepository.findByCart(cart);
    }
}
