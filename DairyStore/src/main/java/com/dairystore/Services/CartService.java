package com.dairystore.Services;

import com.dairystore.Models.Cart;
import com.dairystore.Models.Product;
import com.dairystore.Models.User;
import com.dairystore.Repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final UserService userService;
    private final ProductService productService;
    private final CartItemService cartItemService;

    public void addToCart(Long productId, int quantity) throws Exception {
        User user = userService.getUserByUsername();
        Product product = productService.getProductById(productId);
        if (quantity > product.getQuantity()) {
            throw new Exception("Няма достатъчно количество от продукта в наличност!");
        }

        double totalPrice = 0;
        if (!user.getUsername().isEmpty()) {
            totalPrice = product.getPrice() * quantity - ((product.getPrice() * quantity) * (product.getDiscount() / 100));
            totalPrice = Math.round(totalPrice * 100.0) / 100.0;
        } else {
            totalPrice = product.getPrice() * quantity;
            totalPrice = Math.round(totalPrice * 100.0) / 100.0;
        }

        Cart cart = cartRepository.findByUser(user);
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
        }

        cartItemService.saveCartItems(cart, product, quantity, totalPrice);

        /*product.setQuantity(product.getQuantity() - quantity);
        productService.save(product);*/
    }

    public Cart getCartByUser(User user) {
        return cartRepository.findByUser(user);
    }

}
