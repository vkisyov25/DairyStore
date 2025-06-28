package com.dairystore.Services;

import com.dairystore.Models.User;
import com.dairystore.Models.dtos.UserDto;
import com.dairystore.Models.dtos.UserInformationDto;
import com.dairystore.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = null;
        if (authentication != null && authentication.isAuthenticated()) {
            user = userRepository.findByUsername(authentication.getName());
        }
        return user;
    }

    @Override
    public UserInformationDto getCurrentUserInformation() {
        User user = getCurrentUser();
        return UserInformationDto.builder()
                .username(user.getUsername())
                .authorities(user.getAuthorities())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(user.getAddress())
                .companyName(user.getCompanyName())
                .companyEIK(user.getCompanyEIK())
                .build();
    }

    @Override
    public void updateCurrentUserInformation(User user, UserInformationDto userInformationDto) {
        user.setName(userInformationDto.getName());
        user.setEmail(userInformationDto.getEmail());
        user.setPhone(userInformationDto.getPhone());
        user.setAddress(userInformationDto.getAddress());
        user.setCompanyName(userInformationDto.getCompanyName());
        user.setCompanyEIK(userInformationDto.getCompanyEIK());

        userRepository.save(user);
    }

    @Override
    public void deleteCompanyInfoOnTheCurrentUser() {
        User user = getCurrentUser();
        user.setCompanyName("");
        user.setCompanyEIK("");
        userRepository.save(user);
    }

    @Override
    public void validateUserInformationDto(User user, UserInformationDto userInformationDto, BindingResult bindingResult) {
        companyNameValidation(user, userInformationDto, bindingResult);
        validateCompanyEIK(user, userInformationDto, bindingResult);
        emailValidation(user, userInformationDto, bindingResult);
    }

    private void emailValidation(User user, UserInformationDto userInformationDto, BindingResult bindingResult) {
        if (!user.getEmail().equals(userInformationDto.getEmail())) {
            if (userRepository.existsByEmail(userInformationDto.getEmail())) {
                bindingResult.rejectValue("email", "error.user", "Имейл адресът вече съществува");
            }
        }
    }

    private void validateCompanyEIK(User user, UserInformationDto userInformationDto, BindingResult bindingResult) {
        if (!user.getCompanyEIK().equals(userInformationDto.getCompanyEIK())) {
            if (userRepository.existsByCompanyEIK(userInformationDto.getCompanyEIK())) {
                bindingResult.rejectValue("companyEIK", "error.companyEIK", "EIK вече съществува");
            }
        }
    }

    private void companyNameValidation(User user, UserInformationDto userInformationDto, BindingResult bindingResult) {
        if (!user.getCompanyName().equals(userInformationDto.getCompanyName())) {
            if (userRepository.existsByCompanyName(userInformationDto.getCompanyName())) {
                bindingResult.rejectValue("companyName", "error.companyName", "Името на фирмата вече съществува");
            }
        }
    }

    @Override
    public List<UserDto> getUsers() {
        List<UserDto> allUsersAsDto = userRepository.findAllUsersAsDto();
        return allUsersAsDto.stream().filter(user -> user.getAuthorities().equals("seller") || user.getAuthorities().equals("buyer"))
                .toList();
    }

    @Override
    public void deleteUserById(Long userId) throws Exception {
        isExist(userId);
        userRepository.deleteById(userId);
    }

    private void isExist(Long userId) throws Exception {
        if (!userRepository.existsById(userId)) {
            throw new Exception("Потребителят не съществува в базата данни");
        }
    }
}
