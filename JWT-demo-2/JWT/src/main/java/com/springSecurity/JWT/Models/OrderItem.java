package com.springSecurity.JWT.Models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private int quantity;
    @Column(nullable = false)
    private double totalPrice;
    @Column(nullable = false, name = "cart_id")
    private Long cartId;
    @Column(nullable = false, name = "product_id")
    private Long productId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private Order order;
}
