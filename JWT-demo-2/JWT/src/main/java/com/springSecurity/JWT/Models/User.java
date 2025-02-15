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
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String authorities;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String phone;
    @Column(nullable = false)
    private String address;

    //@Column(unique = true) //Махнах ги заради проблема при регистрация на потребители с роля byuer защото като
    // companyName и companyEIK са празни и в базата данни има потребител с празни полета не може да се запази в базата данни новия потребител
    private String companyName;

    //@Column(unique = true) // както при companyName
    private String companyEIK;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Product> productList;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Cart cart;
}
