package com.springSecurity.JWT.Models;

import jakarta.persistence.*;
import lombok.*;

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
    private long id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(unique = true, nullable = false)
    private String password;
    @Column(unique = false, nullable = false)
    private String authorities;
    @Column(unique = true, nullable = false)
    private String email;
}
