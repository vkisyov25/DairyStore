package com.dairystore.Services.analyses.seller;

import com.dairystore.Models.OrderItem;
import com.dairystore.Models.User;
import com.dairystore.Models.dtos.SellerAnalyticsDto;
import com.dairystore.Services.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TotalEarningsStrategy implements SellerAnalysisStrategy {
    private final OrderItemService orderItemService;

    @Override
    public void analyze(User user, SellerAnalyticsDto.SellerAnalyticsDtoBuilder builder) {
        List<OrderItem> orderItemList = orderItemService.getOrderItemsBySellerId(user.getId());
        double totalEarnings = orderItemList.stream().mapToDouble(e -> e.getTotalPrice()).sum();

        builder.totalEarnings(totalEarnings);
    }
}
