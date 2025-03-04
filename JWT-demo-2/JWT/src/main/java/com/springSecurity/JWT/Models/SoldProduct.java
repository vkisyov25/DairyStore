package com.springSecurity.JWT.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
