package com.dairystore.Security;

import com.dairystore.Models.dtos.CreateUserDto;
import com.dairystore.Models.dtos.LoginUserDto;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;

public interface CustomUserDetailsService extends UserDetailsService {
    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    void deleteCookies();

    void create(CreateUserDto createUserDto);

    void isCompanyInfoValid(String eik, String name, BindingResult bindingResult);

    void validateUser(CreateUserDto user, BindingResult bindingResult);

    boolean existsByEmail(String email);

    boolean existsByCompanyName(String companyName);

    boolean existsByUsername(String username);

    boolean existsByCompanyEIK(String companyEIK);

    String roleExtraction(@NotNull LoginUserDto loginUserDto);

    boolean passwordValidator(@NotNull LoginUserDto loginUserDto);
}
