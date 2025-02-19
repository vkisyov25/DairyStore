package com.springSecurity.JWT.Models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Table(name = "sold_products")
public class SoldProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private int quantity;
    @Column(nullable = false)
    private Long product_id;
    @Column(nullable = false)
    private Long cart_id;
    @Column(nullable = false)
    private double finalPrice;
    @Column(nullable = false)
    private Long buyer_id;
    @Column(nullable = false)
    private Long seller_id;
}
