package com.springSecurity.JWT.Models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "delivery_company")
public class DeliveryCompany {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private double deliveryFee;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "deliveryCompany")
    private List<Order> orderList;

}
