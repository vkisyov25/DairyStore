package com.dairystore.Services.analyses.buyer;

import com.dairystore.Models.OrderItem;
import com.dairystore.Models.User;
import com.dairystore.Models.dtos.BuyerAnalyticsDto;
import com.dairystore.Services.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TotalPurchasePrice implements BuyerAnalysisStrategy {
    private final OrderItemService orderItemService;

    @Override
    public void analyze(User user, BuyerAnalyticsDto.BuyerAnalyticsDtoBuilder builder) {
        List<OrderItem> orderItemList = orderItemService.getOrderItemsByCartId(user.getCart().getId());
        double totalPrice = orderItemList.stream().mapToDouble(product -> product.getTotalPrice()).sum();
        builder.totalPurchasePrice(totalPrice);
    }
}
