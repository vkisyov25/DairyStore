package com.springSecurity.JWT.Repository;

import com.springSecurity.JWT.Models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findTopByUserIdOrderByDateDesc(Long userId);

    @Query("SELECT order FROM Order order WHERE order.userId =?1")
    List<Order> getOrdersByUserId(Long user_id);
}
