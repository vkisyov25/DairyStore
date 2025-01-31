package com.springSecurity.JWT.Validations;

import com.springSecurity.JWT.Models.User;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CompanyEIKValidator implements ConstraintValidator<CompanyEIK, User> {


    @Override
    public boolean isValid(User user, ConstraintValidatorContext context) {
        boolean isValid = true;
        if (user.getAuthorities().equals("seller") && !user.getCompanyEIK().isEmpty() ) {
            if (!user.getCompanyEIK().matches("^[0-9]{9}$")){
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("CompanyEIK must be only 9 digits")
                        .addPropertyNode("companyEIK")
                        .addConstraintViolation();
            }
            isValid = false;
        }

        if (user.getAuthorities().equals("buyer") && !user.getCompanyEIK().isEmpty()) {
            if (!user.getCompanyEIK().matches("^[0-9]{9}$")){
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("CompanyEIK must be only 9 digits")
                        .addPropertyNode("companyEIK")
                        .addConstraintViolation();
            }
            isValid = false;
        }
        return isValid;
    }
}

