package com.dairystore.Services;

import com.dairystore.Models.User;
import com.dairystore.Models.dtos.UserInformationDto;
import com.dairystore.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getUserByUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = null;
        if (authentication != null && authentication.isAuthenticated()) {
            user = userRepository.findByUsername(authentication.getName());
        }
        return user;
    }

    public UserInformationDto getCurrentUserInformation() {
        User user = getUserByUsername();
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

    public void updateCurrentUserInformation(UserInformationDto userInformationDto) {
        User user = getUserByUsername();

        if (userInformationDto.getUsername() != null && !userInformationDto.getUsername().isEmpty()) {
            user.setUsername(userInformationDto.getUsername());
        }
        if (userInformationDto.getName() != null && !userInformationDto.getName().isEmpty()) {
            user.setName(userInformationDto.getName());
        }
        if (userInformationDto.getEmail() != null && !userInformationDto.getEmail().isEmpty()) {
            user.setEmail(userInformationDto.getEmail());
        }
        if (userInformationDto.getPhone() != null && !userInformationDto.getPhone().isEmpty()) {
            user.setPhone(userInformationDto.getPhone());
        }
        if (userInformationDto.getAddress() != null && !userInformationDto.getAddress().isEmpty()) {
            user.setAddress(userInformationDto.getAddress());
        }
        if (userInformationDto.getCompanyName() != null && !userInformationDto.getCompanyName().isEmpty()) {
            user.setCompanyName(userInformationDto.getCompanyName());
        }
        if (userInformationDto.getCompanyEIK() != null && !userInformationDto.getCompanyEIK().isEmpty()) {
            user.setCompanyEIK(userInformationDto.getCompanyEIK());
        }

        /*public void updateCurrentUserInformation(UserInformationDto userInformationDto) {
            User user = getUserByUsername();

            Optional.ofNullable(userInformationDto.getUsername()).filter(s -> !s.isEmpty()).ifPresent(user::setUsername);
            Optional.ofNullable(userInformationDto.getName()).filter(s -> !s.isEmpty()).ifPresent(user::setName);
            Optional.ofNullable(userInformationDto.getEmail()).filter(s -> !s.isEmpty()).ifPresent(user::setEmail);
            Optional.ofNullable(userInformationDto.getPhone()).filter(s -> !s.isEmpty()).ifPresent(user::setPhone);
            Optional.ofNullable(userInformationDto.getAddress()).filter(s -> !s.isEmpty()).ifPresent(user::setAddress);
            Optional.ofNullable(userInformationDto.getCompanyName()).filter(s -> !s.isEmpty()).ifPresent(user::setCompanyName);
            Optional.ofNullable(userInformationDto.getCompanyEIK()).filter(s -> !s.isEmpty()).ifPresent(user::setCompanyEIK);

            userRepository.save(user);
        }*/

        userRepository.save(user);

    }

    public void deleteCompanyInfoOnTheCurrentUser() {
        User user = getUserByUsername();
        user.setCompanyName(null);
        user.setCompanyEIK(null);
        userRepository.save(user);
    }

    public void validateUserInformationDto(UserInformationDto user, BindingResult bindingResult) {

        if (getUserByUsername().getAuthorities().equals("seller")) {
            if (!user.getCompanyName().isEmpty()) {
                if (userRepository.existsByCompanyName(user.getCompanyName())) {
                    bindingResult.rejectValue("companyName", "error.companyName", "Company already exists.");
                }
            }

            if (user.getCompanyEIK() != null) {
                if (userRepository.existsByCompanyEIK(user.getCompanyEIK())) {
                    bindingResult.rejectValue("companyEIK", "error.companyEIK", "Company EIK already exists");
                }
            }

        }

        if (getUserByUsername().getAuthorities().equals("buyer")) {
            if (!user.getCompanyName().isEmpty()) {
                if (userRepository.existsByCompanyName(user.getCompanyName()) && !user.getCompanyName().isEmpty()) {
                    bindingResult.rejectValue("companyName", "error.companyName", "Company already exists.");
                }
            }

            if (user.getCompanyEIK() != null) {
                if (userRepository.existsByCompanyEIK(user.getCompanyEIK()) && !user.getCompanyEIK().isEmpty()) {
                    bindingResult.rejectValue("companyEIK", "error.companyEIK", "Company EIK already exists");
                }
            }

        }

        if (user.getEmail() != null) {
            if (userRepository.existsByEmail(user.getEmail())) {
                bindingResult.rejectValue("email", "error.user", "Email already exists.");
            }
        }

    }
}
