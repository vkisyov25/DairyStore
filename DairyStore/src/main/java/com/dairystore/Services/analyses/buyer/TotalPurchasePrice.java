package com.dairystore.Services.analyses.buyer;

import com.dairystore.Models.Order;
import com.dairystore.Models.OrderItem;
import com.dairystore.Models.User;
import com.dairystore.Models.dtos.BuyerAnalyticsDto;
import com.dairystore.Services.OrderItemService;
import com.dairystore.Services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TotalPurchasePrice implements BuyerAnalysisStrategy {
    private final OrderItemService orderItemService;
    private final OrderService orderService;

    @Override
    public void analyze(User user, BuyerAnalyticsDto.BuyerAnalyticsDtoBuilder builder) {
        List<OrderItem> orderItemList = orderItemService.getOrderItemsByUserId(user.getId());
        double deliveryFee = 0.0;
        HashSet<Long> hashSetOrders = new HashSet<>();
        for (int i = 0; i < orderItemList.size(); i++) {
            hashSetOrders.add(orderItemList.get(i).getOrder().getId());
        }

        for (Long orderId : hashSetOrders) {
            Order order = orderService.getOrderById(orderId);
            deliveryFee += order.getDeliveryCompany().getDeliveryFee();
        }

        double totalPrice = orderItemList.stream().mapToDouble(product -> product.getTotalPrice()).sum();
        totalPrice +=deliveryFee;
        builder.totalPurchasePrice(totalPrice);
    }
}
