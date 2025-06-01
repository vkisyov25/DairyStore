package com.dairystore.Models.dtos;

import com.dairystore.Validations.CompanyEIK;
import com.dairystore.Validations.ValidSellerFields;
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
    @NotBlank(message = "Потребителското име не може да бъде празно")
    private String username;
    @NotBlank(message = "Паролата не може да бъде празна")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@.!%$?&])[A-Za-z\\d@.!%$?&]{8,}$", message = "Паролата трябва да е с дължина поне 8 знака и да съдържа поне: една главна буква, \" + \"една малка буква, едно число и един специален символ (@, $, !, %, , ?, &).")
    private String password;
    @NotBlank
    private String authorities;
    @NotBlank(message = "Имейлът не може да бъде празен")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Невалиден формат")
    @Email
    private String email;
    @NotBlank(message = "Името не моце да бъде празно")
    private String name;
    @NotBlank(message = "Телефонният номер не може да бъде празен")
    @Pattern(regexp = "^[0-9]{10}$", message = "Телефонният номер трябва да е точно 10 цифри")
    private String phone;
    @NotBlank(message = "Адресът не може да бъде празен")
    private String address;

    //@Column(unique = true) //Махнах ги заради проблема при регистрация на потребители с роля byuer защото като
    // companyName и companyEIK са празни и в базата данни има потребител с празни полета не може да се запази в базата данни новия потребител
    private String companyName;

    //@Column(unique = true) // както при companyName
    //@Pattern(regexp = "^[0-9]{9}$")
    private String companyEIK;
    private String accountId;

}
