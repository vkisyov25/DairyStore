package com.dairystore.Services;

import com.dairystore.Models.User;
import com.dairystore.Models.dtos.UserDto;
import com.dairystore.Models.dtos.UserInformationDto;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface UserService {
    User getCurrentUser();

    UserInformationDto getCurrentUserInformation();

    void updateCurrentUserInformation(User user, UserInformationDto userInformationDto);

    void deleteCompanyInfoOnTheCurrentUser();

    void validateUserInformationDto(User user, UserInformationDto userInformationDto, BindingResult bindingResult);

    List<UserDto> getUsers();

    void deleteUserById(Long userId) throws Exception;
}
