package com.springSecurity.JWT.Models.dtos;

import com.springSecurity.JWT.Validations.CompanyEIK;
import com.springSecurity.JWT.Validations.ValidSellerFields;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ValidSellerFields
@CompanyEIK
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateUserDto {
    @NotBlank(message = "Username cannot be blank")
    private String username;
    @NotBlank(message = "Password cannot be blank")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@.!%$?&])[A-Za-z\\d@.!%$?&]{8,}$", message = "The password must be at least 8 characters long and contain at least: one uppercase letter, " + "one lowercase letter, one number, and one special character (@, $, !, %, , ?, &).")
    private String password;
    @NotBlank
    private String authorities;
    @NotBlank(message = "Email cannot be blank")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email format")
    @Email
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

}
