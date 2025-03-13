package com.springSecurity.JWT.Validations;

import com.springSecurity.JWT.Models.dtos.CreateUserDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SellerFieldsValidator implements ConstraintValidator<ValidSellerFields, CreateUserDto> {

    @Override
    public boolean isValid(CreateUserDto createUserDto, ConstraintValidatorContext context) {
        if ("seller".equals(createUserDto.getAuthorities())) {
            boolean isValid = true;

            if (createUserDto.getCompanyName() == null || createUserDto.getCompanyName().isBlank()) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Company name is required for sellers").addPropertyNode("companyName") // Свързваме грешката с конкретно поле
                        .addConstraintViolation();
                isValid = false;
            }

            if (createUserDto.getCompanyEIK() == null || createUserDto.getCompanyEIK().isBlank()) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Company EIK is required for sellers").addPropertyNode("companyEIK") // Свързваме грешката с конкретно поле
                        .addConstraintViolation();
                isValid = false;
            }

            return isValid;
        }

        return true; // Ако не е seller, пропускаме проверката
    }
}
