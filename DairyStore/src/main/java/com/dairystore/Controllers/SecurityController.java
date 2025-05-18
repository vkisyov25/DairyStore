package com.dairystore.Controllers;

import com.dairystore.Models.dtos.CreateUserDto;
import com.dairystore.Models.dtos.LoginUserDto;
import com.dairystore.Security.CustomUserDetailsService;
import com.dairystore.Services.EmailService;
import com.dairystore.Services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/test")
@RequiredArgsConstructor
public class SecurityController {
    private final CustomUserDetailsService customUserDetailsService;
    private final EmailService emailService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> processLogin(@RequestBody LoginUserDto loginUserDto) {
        String role = customUserDetailsService.roleExtraction(loginUserDto);
        try {

            if (!customUserDetailsService.passwordValidator(loginUserDto)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Невалидни данни за вход.");
            }

            if (role.equals("buyer")) {
                return ResponseEntity.ok("redirect:/test/buyer");
            } else if (role.equals("seller")) {
                return ResponseEntity.ok("redirect:/test/seller");
            } else if (role.equals("admin")) {
                return ResponseEntity.ok("redirect:/test/admin");
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Непозната роля.");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Невалидни данни за вход.");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Възникна неочаквана грешка.");
        }

    }

    @PostMapping("/register")
    public ResponseEntity<?> createRegistration(@Valid @RequestBody CreateUserDto createUserDto, BindingResult bindingResult) {
        customUserDetailsService.validateUser(createUserDto, bindingResult);
        customUserDetailsService.isCompanyInfoValid(createUserDto.getCompanyEIK(), createUserDto.getCompanyName(), bindingResult);
        if (bindingResult.hasErrors()) {
            Map<String, List<String>> errors = bindingResult.getFieldErrors()
                    .stream()
                    .collect(Collectors.groupingBy(
                            FieldError::getField,
                            Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())
                    ));

            return ResponseEntity.badRequest().body(Map.of("errors", errors));
        }

        customUserDetailsService.create(createUserDto);
        emailService.sendRegistrationEmail(createUserDto.getEmail(), createUserDto.getName());
        return ResponseEntity.ok(Map.of("message", "Успешна регистрация"));
    }

    @GetMapping("/seller")
    public String sellerPage(Model model) {
        model.addAttribute("seller", "Здравей, " + userService.getUserByUsername().getName());
        return "sellerPage";
    }

    @GetMapping("/buyer")
    public String buyerPage(Model model) {
        model.addAttribute("buyer", "Здравей, " + userService.getUserByUsername().getName());
        return "buyerPage";
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "adminPage";
    }

    @PostMapping("/logout")
    public String logout() {
        customUserDetailsService.deleteCookies();
        return "redirect:/html/homePage.html";
    }

    @GetMapping("/tokenExpiration")
    public String showInvalidToken() {
        customUserDetailsService.deleteCookies();
        return "tokenExpirationPage";
    }

}
