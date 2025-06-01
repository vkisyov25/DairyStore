package com.dairystore.Services.analyses.buyer;

import com.dairystore.Models.OrderItem;
import com.dairystore.Models.User;
import com.dairystore.Models.dtos.BuyerAnalyticsDto;
import com.dairystore.Services.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MostPurchasedType implements BuyerAnalysisStrategy {
    private final OrderItemService orderItemService;

    @Override
    public void analyze(User user, BuyerAnalyticsDto.BuyerAnalyticsDtoBuilder builder) {
        List<OrderItem> orderItemList = orderItemService.getOrderItemsByUserId(user.getId());

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
        String mostPurchasedType = categoryCountMap.entrySet().stream().max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("");

        builder.mostPurchasedType(mostPurchasedType);
    }
}
