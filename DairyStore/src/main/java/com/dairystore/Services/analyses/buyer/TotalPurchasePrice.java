package com.dairystore.Services.analyses.buyer;

import com.dairystore.Models.OrderItem;
import com.dairystore.Models.User;
import com.dairystore.Models.dtos.BuyerAnalyticsDto;
import com.dairystore.Services.OrderItemService;
import com.dairystore.Services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.dairystore.Services.analyses.buyer.AveragePurchasePriceStrategy.deliveryFeeSum;

@Component
@RequiredArgsConstructor
public class TotalPurchasePrice implements BuyerAnalysisStrategy {
    private final OrderItemService orderItemService;
    private final OrderService orderService;

    @Override
    public void analyze(User user, BuyerAnalyticsDto.BuyerAnalyticsDtoBuilder builder) {
        List<OrderItem> orderItemList = orderItemService.getOrderItemsByUserId(user.getId());

        double totalPrice = orderItemList.stream().mapToDouble(product -> product.getTotalPrice()).sum();
        totalPrice += deliveryFeeSum(orderItemList, orderService);
        builder.totalPurchasePrice(totalPrice);
    }
}
