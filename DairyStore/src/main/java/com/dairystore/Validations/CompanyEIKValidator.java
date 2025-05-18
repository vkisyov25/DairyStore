package com.dairystore.Validations;

import com.dairystore.Models.dtos.CreateUserDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CompanyEIKValidator implements ConstraintValidator<CompanyEIK, CreateUserDto> {


    @Override
    public boolean isValid(CreateUserDto createUserDto, ConstraintValidatorContext context) {
        boolean isValid = true;
        if (createUserDto.getAuthorities().equals("seller")) {
            if (!createUserDto.getCompanyEIK().matches("^[0-9]{9}$")) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("ЕИК-то на фирмата трябва да е точно 9 цифри").addPropertyNode("companyEIK").addConstraintViolation();
                isValid = false;
            }

        }

        if (createUserDto.getAuthorities().equals("buyer") && !createUserDto.getCompanyEIK().isEmpty()) {
            if (!createUserDto.getCompanyEIK().matches("^[0-9]{9}$")) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("ЕИК-то на фирмата трябва да е точно 9 цифри").addPropertyNode("companyEIK").addConstraintViolation();
                isValid = false;
            }

        }
        return isValid;
    }
}

