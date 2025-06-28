package com.dairystore.Repository;

import com.dairystore.Models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("SELECT orderItem FROM OrderItem orderItem WHERE orderItem.order.id = ?1")
    List<OrderItem> findOrderItemsByOrderId(Long orderId);

    List<OrderItem> findOrderItemsBySellerId(Long sellerId);
}
