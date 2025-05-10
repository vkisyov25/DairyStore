package com.dairystore.Models.dtos;

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
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Невалиден формат на имейл адреса")
    private String email;
    @Pattern(regexp = "^([А-Яа-яЁё]+ [А-Яа-яЁё]+|[A-Za-z]+ [A-Za-z]+)$", message = "Името трябва да съдържа само букви")
    private String name;
    @Pattern(regexp = "^[0-9]{10}$", message = "Телефонният номер трябва да е точно 10 цифри.")
    private String phone;
    private String address;
    private String companyName;
    @Pattern(regexp = "^[0-9]{9}$", message = "EIK номерът на компанията трябва да е точно 9 цифри.")
    private String companyEIK;
}
