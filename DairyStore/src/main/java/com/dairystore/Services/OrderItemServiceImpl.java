package com.dairystore.Services;

import com.dairystore.Models.Order;
import com.dairystore.Models.OrderItem;
import com.dairystore.Repository.OrderItemRepository;
import com.dairystore.Repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepository orderItemRepository;

    private final OrderRepository orderRepository;

    @Override
    public void saveOrderItem(OrderItem orderItem) {
        orderItemRepository.save(orderItem);
    }

    @Override
    public List<OrderItem> getOrderItemsByOrderId(Long orderId) {
        return orderItemRepository.findOrderItemsByOrderId(orderId);
    }

    @Override
    public List<OrderItem> getOrderItemsByUserId(Long userId) {
        List<OrderItem> orderItemList = new ArrayList<>();
        List<Order> orderList = orderRepository.findOrdersByUserId(userId);
        for (int i = 0; i < orderList.size(); i++) {
            List<OrderItem> orderItemsByOrderId = orderItemRepository.findOrderItemsByOrderId(orderList.get(i).getId());
            for (int j = 0; j < orderItemsByOrderId.size(); j++) {
                OrderItem orderItem = OrderItem.builder()
                        .id(orderItemsByOrderId.get(j).getId())
                        .quantity(orderItemsByOrderId.get(j).getQuantity())
                        .totalPrice(orderItemsByOrderId.get(j).getTotalPrice())
                        .name(orderItemsByOrderId.get(j).getName())
                        .type(orderItemsByOrderId.get(j).getType())
                        .weight(orderItemsByOrderId.get(j).getWeight())
                        .price(orderItemsByOrderId.get(j).getPrice())
                        .description(orderItemsByOrderId.get(j).getDescription())
                        .discount(orderItemsByOrderId.get(j).getDiscount())
                        .sellerId(orderItemsByOrderId.get(j).getSellerId())
                        .order(orderItemsByOrderId.get(j).getOrder())
                        .build();
                orderItemList.add(orderItem);
            }
        }
        return orderItemList;
    }

    @Override
    public List<OrderItem> getOrderItemsBySellerId(Long sellerId) {
        return orderItemRepository.findOrderItemsBySellerId(sellerId);
    }
}
