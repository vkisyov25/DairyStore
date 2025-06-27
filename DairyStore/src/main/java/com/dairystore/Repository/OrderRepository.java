package com.dairystore.Repository;

import com.dairystore.Models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findTopByUserIdOrderByDateDesc(Long userId);

    @Query("SELECT order FROM Order order WHERE order.user.id =?1")
    List<Order> findOrdersByUserId(Long user_id);

    @Query("SELECT order FROM Order order WHERE order.id =?1")
    Order findOrderById(Long user_id);
    /*Order findOrdersByUserId(Long userId);*/
}
