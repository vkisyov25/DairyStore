package com.dairystore.Validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CompanyEIKValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CompanyEIK {
    String message() default "Fields companyEIK must be only 9 digits";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
