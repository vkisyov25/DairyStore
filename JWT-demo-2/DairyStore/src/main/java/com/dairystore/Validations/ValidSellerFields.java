package com.dairystore.Validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = SellerFieldsValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidSellerFields {
    String message() default "Fields companyName and companyEIK are required for sellers";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
