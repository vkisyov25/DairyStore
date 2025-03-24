package com.dairystore.Services;

import com.dairystore.Models.OrderItem;
import com.dairystore.Models.User;
import com.dairystore.Models.dtos.BuyerAnalyticsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserAnalyticsService {
    private final OrderItemService orderItemService;
    private final OrderService orderService;
    private final UserService userService;

    public BuyerAnalyticsDto analyzeUserShoppingBehavior() {
        User user = userService.getUserByUsername();
        List<OrderItem> orderItemList = orderItemService.getOrderItemsByCartId(user.getCart().getId());
        double totalPrice = orderItemList.stream().mapToDouble(product -> product.getTotalPrice()).sum();

        Map<String, Integer> categoryCountMap = new HashMap<>();
        for (int i = 0; i < orderItemList.size(); i++) {
            int count = 0;
            if (categoryCountMap.containsKey(orderItemList.get(i).getType())) {
                count = categoryCountMap.get(orderItemList.get(i).getType());
                count += orderItemList.get(i).getQuantity();
                categoryCountMap.put(orderItemList.get(i).getType(), count);
            } else {
                categoryCountMap.put(orderItemList.get(i).getType(), orderItemList.get(i).getQuantity());
            }
        }
        String mostPurchasedType = categoryCountMap.entrySet().stream().max(Map.Entry.comparingByValue()) // Намираме entry-то с най-висока стойност
                .map(Map.Entry::getKey) // Вземаме ключа (категорията)
                .orElse(""); // Премахва Optional и връща само стойността


        int ordersCount = orderService.getOrdersByUserId(user.getId()).size();

        double averagePurchasePrice = totalPrice / ordersCount;

        return BuyerAnalyticsDto.builder().totalPurchasePrice(totalPrice).averagePurchasePrice(averagePurchasePrice).mostPurchasedType(mostPurchasedType).build();

    }
}
