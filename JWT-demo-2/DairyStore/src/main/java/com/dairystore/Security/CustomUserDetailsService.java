package com.dairystore.Security;

import com.dairystore.Models.User;
import com.dairystore.Models.dtos.CreateUserDto;
import com.dairystore.Models.dtos.LoginUserDto;
import com.dairystore.Repository.UserRepository;
import com.dairystore.Utils.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final HttpServletResponse response;
    private final HttpServletRequest request;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        // Зареждане на данни за потребителя
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        // Генериране на JWT токен
        String token = jwtUtil.generateToken(customUserDetails);

        String role = customUserDetails.getAuthorities().iterator().next().getAuthority();
        //Collection<? extends GrantedAuthority> cc = authentication.getAuthorities();


        deleteCookies();

        // Съхраняване на JWT токен в HTTP-only cookie
        Cookie cookie = new Cookie("JWT", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(3600); // 1 час
        response.addCookie(cookie);
        return new CustomUserDetails(user);


    }

    public void deleteCookies() {
        request.getSession().invalidate(); // Това ще изтрие текущата сесия
        // Изтриваме стария токен, ако има такъв
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("JWT")) {
                    cookie.setValue(null); // Изтриваме стойността
                    cookie.setMaxAge(0);   // Изтриваме cookie-то
                    cookie.setPath("/");   // Уверяваме се, че е изтрито на глобално ниво
                    response.addCookie(cookie); // Изпращаме обратно изтритото cookie
                }
            }
        }
    }

    public void create(CreateUserDto createUserDto) {
        User user = mapToUser(createUserDto);
        user.setPassword(passwordEncoder.encode(createUserDto.getPassword()));
        userRepository.save(user);
    }

    private User mapToUser(CreateUserDto dto) {
        return User.builder().username(dto.getUsername()).password(dto.getPassword()).email(dto.getEmail()).name(dto.getName()).phone(dto.getPhone()).address(dto.getAddress()).companyName(dto.getCompanyName()).companyEIK(dto.getCompanyEIK()).authorities(dto.getAuthorities()).build();
    }


    public void validateUser(CreateUserDto user, BindingResult bindingResult) {

        //проверката с ролите е заради това, че при buyer companyName и companyEIK могат да са празни и като има потребител с празни
        // companyName и companyEIK в базата данни и като искам да запиша нов ми показва, че вече съществуват
        if (user.getAuthorities().equals("seller")) {
            //По принцип seller няма как да има записани потребители с празно companyName, но метода existsByCompanyName() проверява за
            //всички потребители а buyer могат да имат и затова слагам !user.getCompanyName().isEmpty()
            if (existsByCompanyName(user.getCompanyName()) && !user.getCompanyName().isEmpty()) {
                bindingResult.rejectValue("companyName", "error.companyName", "Company already exists.");
            }

            if (existsByCompanyEIK(user.getCompanyEIK()) && !user.getCompanyEIK().isEmpty()) {
                bindingResult.rejectValue("companyEIK", "error.companyEIK", "Company EIK already exists");
            }

        }

        if (user.getAuthorities().equals("buyer")) {
            if (existsByCompanyName(user.getCompanyName()) && !user.getCompanyName().isEmpty()) {
                bindingResult.rejectValue("companyName", "error.companyName", "Company already exists.");
            }

            if (existsByCompanyEIK(user.getCompanyEIK()) && !user.getCompanyEIK().isEmpty()) {
                bindingResult.rejectValue("companyEIK", "error.companyEIK", "Company EIK already exists");
            }

        }

        if (existsByEmail(user.getEmail())) {
            bindingResult.rejectValue("email", "error.user", "Email already exists.");
        }

        if (existsByUsername(user.getUsername())) {
            bindingResult.rejectValue("username", "error.user", "Username already exists.");
        }
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean existsByCompanyName(String companyName) {
        return userRepository.existsByCompanyName(companyName);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByCompanyEIK(String companyEIK) {
        return userRepository.existsByCompanyEIK(companyEIK);
    }

    public String roleExtraction(@NotNull LoginUserDto loginUserDto) {
        String username = loginUserDto.getUsername();
        UserDetails userDetails = loadUserByUsername(username);
        return userDetails.getAuthorities().iterator().next().getAuthority();
    }

    public boolean passwordValidator(@NotNull LoginUserDto loginUserDto) {
        String username = loginUserDto.getUsername();
        String password = loginUserDto.getPassword();
        UserDetails userDetails = loadUserByUsername(username);
        String storedPassword = userDetails.getPassword();
        return passwordEncoder.matches(password, storedPassword);

    }
}
