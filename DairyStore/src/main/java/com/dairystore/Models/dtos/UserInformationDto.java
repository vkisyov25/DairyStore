package com.dairystore.Models.dtos;

import com.dairystore.Validations.CompanyEIKDto;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@CompanyEIKDto
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
    private String companyEIK;
}
