package com.springSecurity.JWT.Services;

import com.springSecurity.JWT.Models.Cart;
import com.springSecurity.JWT.Models.Product;
import com.springSecurity.JWT.Models.User;
import com.springSecurity.JWT.Repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final UserService userService;
    private final ProductService productService;
    private final CartItemService cartItemService;

    @Autowired
    public CartService(CartRepository cartRepository, UserService userService, ProductService productService, CartItemService cartItemService) {
        this.cartRepository = cartRepository;
        this.userService = userService;
        this.productService = productService;
        this.cartItemService = cartItemService;
    }

    public void addToCart(Long productId, int quantity) {
        User user = userService.getUserByUsername();
        Product product = productService.getProductById(productId);
        double totalPrice = product.getPrice() * quantity - ((product.getPrice() * quantity) * (product.getDiscount() / 100));

        //Проверява дали потребителя има количка - ако няма я създава
        Cart cart = cartRepository.findByUser(user);
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
        }

        /*//Създава и добавя в базата данни CartItem
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        cartItem.setTotalPrice(totalPrice);
        cartItemRepository.save(cartItem);*/
        //TODO: Is this a good practise
        cartItemService.saveCartItems(cart, product, quantity, totalPrice);

        /*//Създава и добавя в базата данни SoldProduct
        SoldProduct soldProduct = SoldProduct.builder()
                .quantity(quantity)
                .product_id(productId)
                .cart_id(cart.getId())
                .finalPrice(totalPrice)
                .buyer_id(user.getId())
                .seller_id(product.getUser().getId())
                .build();
        soldProductRepository.save(soldProduct);*/
       /* //TODO: Is this a good practise
        soldProductService.saveSoldProduct(cart, quantity, productId, user, product, totalPrice);*/

    }

    public Cart getCartByUser(User user) {
        return cartRepository.findByUser(user);
    }

}
