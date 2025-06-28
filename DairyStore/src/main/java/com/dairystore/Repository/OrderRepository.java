package com.dairystore.Repository;

import com.dairystore.Models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT order FROM Order order WHERE order.user.id =?1")
    List<Order> findOrdersByUserId(Long user_id);

    @Query("SELECT order FROM Order order WHERE order.id =?1")
    Order findOrderById(Long user_id);
}
