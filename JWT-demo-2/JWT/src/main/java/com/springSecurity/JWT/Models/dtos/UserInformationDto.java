package com.springSecurity.JWT.Models.dtos;

import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserInformationDto {
    private String username;
    private String authorities;
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email format")
    private String email;
    @Pattern(regexp = "^[A-z a-z]+$", message = "Name must not contain numbers")
    private String name;
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be exactly 10 digits.")
    private String phone;
    private String address;
    private String companyName;
    @Pattern(regexp = "^[0-9]{9}$", message = "Company EIK must be exactly 9 digits.")
    private String companyEIK;
}
