package com.springSecurity.JWT.Models;

import com.springSecurity.JWT.Models.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String deliveryAddress;
    @Enumerated(EnumType.STRING)  // Записва се като текст в базата
    @Column(nullable = false)
    private PaymentMethod paymentMethod;
    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private double totalPrice;

    @Column(nullable = false, name = "user_id")
    private Long userId;
    @ManyToOne
    @JoinColumn(name = "delivery_company_id")
    private DeliveryCompany deliveryCompany;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    List<OrderItem> orderItemList;

}
