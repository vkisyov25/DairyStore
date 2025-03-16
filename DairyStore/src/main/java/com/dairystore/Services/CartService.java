package com.dairystore.Services;

import com.dairystore.Models.Cart;
import com.dairystore.Models.CartItem;
import com.dairystore.Models.Product;
import com.dairystore.Models.User;
import com.dairystore.Models.dtos.ShoppingCartDto;
import com.dairystore.Repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        if (!user.getCompanyName().isEmpty()) {
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

    public List<ShoppingCartDto> viewShoppingCart() {
        User user = userService.getUserByUsername();
        List<CartItem> cartItemList = cartItemService.getCartItemsByCart(user.getCart());

        return cartItemList.stream()
                .map(cartItem -> {
                    double discount = user.getCompanyName().isEmpty() ? 0 : cartItem.getProduct().getDiscount();
                    double totalPricePerProduct = (cartItem.getQuantity() * cartItem.getProduct().getPrice()) - ((cartItem.getQuantity() * cartItem.getProduct().getPrice()) * (discount / 100));
                    totalPricePerProduct = Math.round(totalPricePerProduct * 100.0) / 100.0;

                    return ShoppingCartDto.builder()
                            .id(cartItem.getProduct().getId())
                            .name(cartItem.getProduct().getName())
                            .type(cartItem.getProduct().getType())
                            .weight(cartItem.getProduct().getWeight())
                            .discount(discount)
                            .price(cartItem.getProduct().getPrice())
                            .quantity(cartItem.getQuantity())
                            .totalPricePerProduct(totalPricePerProduct)
                            .build();
                })
                .collect(Collectors.toList());
    }

    public Cart getCartByUser(User user) {
        return cartRepository.findByUser(user);
    }

}
