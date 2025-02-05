package com.springSecurity.JWT.Models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int quantity;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cart") //cart е името на полето в Product, което съдържа релацията.
    private List<Product> productList;
}
