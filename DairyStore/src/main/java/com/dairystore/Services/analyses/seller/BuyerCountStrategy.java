package com.dairystore.Services.analyses.seller;

import com.dairystore.Models.OrderItem;
import com.dairystore.Models.User;
import com.dairystore.Models.dtos.SellerAnalyticsDto;
import com.dairystore.Services.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class BuyerCountStrategy implements SellerAnalysisStrategy {
    private final OrderItemService orderItemService;

    @Override
    public void analyze(User user, SellerAnalyticsDto.SellerAnalyticsDtoBuilder builder) {

        List<OrderItem> orderItemList = orderItemService.getOrderItemsBySellerId(user.getId());
        Set<Long> uniqueCartIds = new HashSet<>();
        for (int i = 0; i < orderItemList.size(); i++) {
            /*uniqueCartIds.add(orderItemList.get(i).getCart().getId());*/
            uniqueCartIds.add(orderItemList.get(i).getOrder().getUser().getId());
        }

        builder.buyerCount(uniqueCartIds.size());
    }
}
