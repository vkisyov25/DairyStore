package com.dairystore.Services;

import com.dairystore.Models.OrderItem;
import com.dairystore.Repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;

    public void saveOrderItem(OrderItem orderItem) {
        orderItemRepository.save(orderItem);
    }

    public List<OrderItem> getOrderItemsByOrderId(Long orderId) {
        return orderItemRepository.findOrderItemsByOrderId(orderId);
    }

    public List<OrderItem> getOrderItemsByCartId(Long cartId) {
        return orderItemRepository.findOrderItemsByCartId(cartId);
    }

    public List<OrderItem> getOrderItemsBySellerId(Long sellerId) {
        return orderItemRepository.findOrderItemsBySellerId(sellerId);
    }
}
