package com.dairystore.Services;

import com.dairystore.Models.OrderItem;

import java.util.List;

public interface OrderItemService {
    void saveOrderItem(OrderItem orderItem);

    List<OrderItem> getOrderItemsByOrderId(Long orderId);

    List<OrderItem> getOrderItemsByUserId(Long cartId);

    List<OrderItem> getOrderItemsBySellerId(Long sellerId);
}
