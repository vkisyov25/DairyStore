package com.dairystore.Models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
