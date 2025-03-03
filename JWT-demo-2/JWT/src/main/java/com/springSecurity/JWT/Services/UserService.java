package com.springSecurity.JWT.Services;

import com.springSecurity.JWT.Models.User;
import com.springSecurity.JWT.Models.dtos.UserInformationDto;
import com.springSecurity.JWT.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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

        userRepository.save(user);

    }

    public void deleteCompanyInfoOnTheCurrentUser() {
        User user = getUserByUsername();
        user.setCompanyName(null);
        user.setCompanyEIK(null);
        userRepository.save(user);
    }
}
