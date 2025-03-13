package com.springSecurity.JWT.Services;

import com.springSecurity.JWT.Models.Cart;
import com.springSecurity.JWT.Models.Product;
import com.springSecurity.JWT.Models.SoldProduct;
import com.springSecurity.JWT.Models.User;
import com.springSecurity.JWT.Models.dtos.BuyerAnalyticsDto;
import com.springSecurity.JWT.Repository.OrderRepository;
import com.springSecurity.JWT.Repository.ProductRepository;
import com.springSecurity.JWT.Repository.SoldProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SoldProductService {
    private final SoldProductRepository soldProductRepository;
    private final UserService userService;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public void saveSoldProduct(Cart cart, int quantity, Long productId, User user, Product product, double totalPrice) {
        SoldProduct soldProduct = SoldProduct.builder().quantity(quantity).product_id(productId).cart_id(cart.getId()).finalPrice(totalPrice).buyer_id(user.getId()).seller_id(product.getUser().getId()).build();
        soldProductRepository.save(soldProduct);
    }

    public BuyerAnalyticsDto analyzeUserShoppingBehavior() {
        User user = userService.getUserByUsername();
        List<SoldProduct> soldProductList = soldProductRepository.findSoldProductByBuyer_id(user.getId());
        double totalPrice = soldProductList.stream().mapToDouble(product -> product.getFinalPrice()).sum();

        Map<String, Integer> categoryCountMap = new HashMap<>();
        for (int i = 0; i < soldProductList.size(); i++) {
            int count = 0;
            Product product = productRepository.findProductById(soldProductList.get(i).getProduct_id());
            assert false;
            if (categoryCountMap.containsKey(product.getType())) {
                count = categoryCountMap.get(product.getType());
                count += soldProductList.get(i).getQuantity();
                categoryCountMap.put(product.getType(), count);
            } else {
                categoryCountMap.put(product.getType(), soldProductList.get(i).getQuantity());
            }
        }
        assert false;
        String mostPurchasedType = categoryCountMap.entrySet().stream().max(Map.Entry.comparingByValue()) // Намираме entry-то с най-висока стойност
                .map(Map.Entry::getKey) // Вземаме ключа (категорията)
                .orElse(""); // Премахва Optional и връща само стойността


        int ordersCount = orderRepository.findOrdersByUserId(user.getId()).size();

        double averagePurchasePrice = totalPrice / ordersCount;

        return BuyerAnalyticsDto.builder().totalPurchasePrice(totalPrice).averagePurchasePrice(averagePurchasePrice).mostPurchasedType(mostPurchasedType).build();

    }
}
