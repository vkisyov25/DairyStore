package com.springSecurity.JWT.Services;

import com.springSecurity.JWT.Models.Cart;
import com.springSecurity.JWT.Models.Product;
import com.springSecurity.JWT.Models.SoldProduct;
import com.springSecurity.JWT.Models.User;
import com.springSecurity.JWT.Repository.SoldProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SoldProductService {
    private final SoldProductRepository soldProductRepository;

    @Autowired
    public SoldProductService(SoldProductRepository soldProductRepository) {
        this.soldProductRepository = soldProductRepository;
    }

    public void saveSoldProduct(Cart cart, int quantity, Long productId, User user, Product product, double totalPrice) {
        SoldProduct soldProduct = SoldProduct.builder()
                .quantity(quantity)
                .product_id(productId)
                .cart_id(cart.getId())
                .finalPrice(totalPrice)
                .buyer_id(user.getId())
                .seller_id(product.getUser().getId())
                .build();
        soldProductRepository.save(soldProduct);
    }
}
