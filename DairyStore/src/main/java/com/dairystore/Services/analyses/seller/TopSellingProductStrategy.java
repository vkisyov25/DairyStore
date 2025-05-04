package com.dairystore.Services.analyses.seller;

import com.dairystore.Models.OrderItem;
import com.dairystore.Models.User;
import com.dairystore.Models.dtos.SellerAnalyticsDto;
import com.dairystore.Services.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TopSellingProductStrategy implements SellerAnalysisStrategy {
    private final OrderItemService orderItemService;

    @Override
    public void analyze(User user, SellerAnalyticsDto.SellerAnalyticsDtoBuilder builder) {

        List<OrderItem> orderItemList = orderItemService.getOrderItemsBySellerId(user.getId());
        Map<String, Integer> productQuantitiesMap = new HashMap<>();
        for (int i = 0; i < orderItemList.size(); i++) {
            int count = 0;
            if (productQuantitiesMap.containsKey(orderItemList.get(i).getName())) {
                count = productQuantitiesMap.get(orderItemList.get(i).getName());
                count += 1;
                productQuantitiesMap.put(orderItemList.get(i).getName(), count);
            } else {
                productQuantitiesMap.put(orderItemList.get(i).getName(), 1);
            }
        }

        String topSellingProduct = productQuantitiesMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("");

        builder.topSellingProduct(topSellingProduct);
    }
}
