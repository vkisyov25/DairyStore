package com.springSecurity.JWT.Models;

import com.springSecurity.JWT.Validations.CompanyEIK;
import com.springSecurity.JWT.Validations.ValidSellerFields;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import java.util.List;

@ValidSellerFields
@CompanyEIK
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

    @NotBlank(message = "Username cannot be blank")
    @Column(unique = true)
    private String username;

    @NotBlank(message = "Password cannot be blank")
    private String password;

    @NotBlank
    private String authorities;

    @NotBlank(message = "Email cannot be blank")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email format")
    @Email
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "Phone number cannot be blank.")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be exactly 10 digits.")
    private String phone;

    @NotBlank(message = "Address cannot be blank")
    private String address;

    //@Column(unique = true) //Махнах ги заради проблема при регистрация на потребители с роля byuer защото като
    // companyName и companyEIK са празни и в базата данни има потребител с празни полета не може да се запази в базата данни новия потребител
    private String companyName;

    //@Column(unique = true) // както при companyName
    //@Pattern(regexp = "^[0-9]{9}$")
    private String companyEIK;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Product> productList;
}
