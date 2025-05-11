package com.dairystore.Services;

import com.dairystore.Models.OrderItem;
import com.dairystore.Repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepository orderItemRepository;

    @Override
    public void saveOrderItem(OrderItem orderItem) {
        orderItemRepository.save(orderItem);
    }

    @Override
    public List<OrderItem> getOrderItemsByOrderId(Long orderId) {
        return orderItemRepository.findOrderItemsByOrderId(orderId);
    }

    @Override
    public List<OrderItem> getOrderItemsByCartId(Long cartId) {
        return orderItemRepository.findOrderItemsByCartId(cartId);
    }

    @Override
    public List<OrderItem> getOrderItemsBySellerId(Long sellerId) {
        return orderItemRepository.findOrderItemsBySellerId(sellerId);
    }
}
