package com.dairystore.Services;

import com.dairystore.Models.Cart;
import com.dairystore.Models.CartItem;
import com.dairystore.Models.Product;
import com.dairystore.Models.User;
import com.dairystore.Models.dtos.ShoppingCartDto;
import com.dairystore.Repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
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
        validateProductQuantity(quantity, product);

        double totalPrice = calculateTotalPrice(quantity, user, product);

        Cart cart = findOrCreateCart(user);

        cartItemService.saveCartItems(cart, product, quantity, totalPrice);

    }

    private  void validateProductQuantity(int quantity, Product product) throws Exception {
        if (quantity > product.getQuantity()) {
            throw new Exception("Няма достатъчно количество от продукта в наличност!");
        }
    }


    private double calculateTotalPrice(int quantity, User user, Product product) {
        double totalPrice = 0;
        if (!user.getCompanyName().isEmpty()) {
            totalPrice = product.getPrice() * quantity - ((product.getPrice() * quantity) * (product.getDiscount() / 100));
            totalPrice = Math.round(totalPrice * 100.0) / 100.0;
        } else {
            totalPrice = product.getPrice() * quantity;
            totalPrice = Math.round(totalPrice * 100.0) / 100.0;
        }
        return totalPrice;
    }

    @NotNull
    private Cart findOrCreateCart(User user) {
        Cart cart = cartRepository.findByUser(user);
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
        }
        return cart;
    }

    public List<ShoppingCartDto> viewShoppingCart() {
        User user = userService.getUserByUsername();
        List<CartItem> cartItemList = cartItemService.getCartItemsByCart(user.getCart());

        return cartItemList.stream()
                .map(cartItem -> mapToShoppingCartDto(user, cartItem))
                .collect(Collectors.toList());
    }

    private ShoppingCartDto mapToShoppingCartDto(User user, CartItem cartItem) {
        Product product = cartItem.getProduct();
        int quantity = cartItem.getQuantity();
        double discount = user.getCompanyName().isEmpty() ? 0 : product.getDiscount();
        double totalPricePerProduct = calculateTotalPricePerProduct(user, product, quantity);

        return ShoppingCartDto.builder()
                .id(product.getId())
                .name(product.getName())
                .type(product.getType())
                .weight(product.getWeight())
                .discount(discount)
                .price(product.getPrice())
                .quantity(quantity)
                .totalPricePerProduct(totalPricePerProduct)
                .build();
    }

    private double calculateTotalPricePerProduct(User user, Product product, int quantity) {
        double basePrice = product.getPrice() * quantity;
        double discountAmount = user.getCompanyName().isEmpty() ? 0 : basePrice * (product.getDiscount() / 100);
        double total = basePrice - discountAmount;
        return Math.round(total * 100.0) / 100.0;
    }

    public Cart getCartByUser(User user) {
        return cartRepository.findByUser(user);
    }

}
