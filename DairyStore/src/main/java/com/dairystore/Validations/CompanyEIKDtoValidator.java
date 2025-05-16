package com.dairystore.Validations;

import com.dairystore.Models.User;
import com.dairystore.Models.dtos.UserInformationDto;
import com.dairystore.Services.UserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CompanyEIKDtoValidator implements ConstraintValidator<CompanyEIKDto, UserInformationDto> {
    private final UserService userService;

    @Override
    public boolean isValid(UserInformationDto createUserDto, ConstraintValidatorContext context) {
        User currentUser = userService.getUserByUsername();
        boolean isValid = true;
        if (currentUser.getAuthorities().equals("seller")) {
            if (!createUserDto.getCompanyEIK().matches("^[0-9]{9}$")) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("CompanyEIK must be only 9 digits").addPropertyNode("companyEIK").addConstraintViolation();
                isValid = false;
            }

        }

        if (currentUser.getAuthorities().equals("buyer") && !createUserDto.getCompanyEIK().isEmpty()) {
            if (!createUserDto.getCompanyEIK().matches("^[0-9]{9}$")) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("CompanyEIK must be only 9 digits").addPropertyNode("companyEIK").addConstraintViolation();
                isValid = false;
            }

        }
        return isValid;
    }
}



