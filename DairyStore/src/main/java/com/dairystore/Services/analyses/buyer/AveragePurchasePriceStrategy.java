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
public class AveragePurchasePriceStrategy implements BuyerAnalysisStrategy {
    private final OrderItemService orderItemService;
    private final OrderService orderService;

    @Override
    public void analyze(User user, BuyerAnalyticsDto.BuyerAnalyticsDtoBuilder builder) {
        List<OrderItem> orderItemList = orderItemService.getOrderItemsByUserId(user.getId());
        double totalPrice = orderItemList.stream().mapToDouble(product -> product.getTotalPrice()).sum();
        int ordersCount = orderService.getOrdersByUserId(user.getId()).size();

        double deliveryFeeSum = deliveryFeeSum(orderItemList, orderService);

        double averagePurchasePrice = (totalPrice + deliveryFeeSum) / ordersCount;
        builder.averagePurchasePrice(averagePurchasePrice);
    }

    static double deliveryFeeSum(List<OrderItem> orderItemList, OrderService orderService) {
        double deliveryFee = 0.0;
        HashSet<Long> hashSetOrders = new HashSet<>();
        for (int i = 0; i < orderItemList.size(); i++) {
            hashSetOrders.add(orderItemList.get(i).getOrder().getId());
        }

        for (Long orderId : hashSetOrders) {
            Order order = orderService.getOrderById(orderId);
            deliveryFee += order.getDeliveryCompany().getDeliveryFee();
        }

        return deliveryFee;
    }
}
