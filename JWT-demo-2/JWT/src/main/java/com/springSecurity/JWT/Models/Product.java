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
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String type;
    @Column(nullable = false)
    private Double price;
    @Column(nullable = false)
    private String description;
    @ManyToOne
    //@JoinColumn(name = "user_id")
    private User user;

}
