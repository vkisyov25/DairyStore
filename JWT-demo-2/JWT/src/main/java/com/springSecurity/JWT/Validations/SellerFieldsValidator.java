package com.springSecurity.JWT.Validations;

import com.springSecurity.JWT.Models.User;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SellerFieldsValidator implements ConstraintValidator<ValidSellerFields, User> {

    @Override
    public boolean isValid(User user, ConstraintValidatorContext context) {
        if ("seller".equals(user.getAuthorities())) {
            boolean isValid = true;

            if (user.getCompanyName() == null || user.getCompanyName().isBlank()) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Company name is required for sellers")
                        .addPropertyNode("companyName") // Свързваме грешката с конкретно поле
                        .addConstraintViolation();
                isValid = false;
            }

            if (user.getCompanyEIK() == null || user.getCompanyEIK().isBlank()) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Company EIK is required for sellers")
                        .addPropertyNode("companyEIK") // Свързваме грешката с конкретно поле
                        .addConstraintViolation();
                isValid = false;
            }

            return isValid;
        }
        return true; // Ако не е seller, пропускаме проверката
    }
}
