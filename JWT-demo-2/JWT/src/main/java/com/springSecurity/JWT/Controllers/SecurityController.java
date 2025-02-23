package com.springSecurity.JWT.Controllers;

import com.springSecurity.JWT.Models.User;
import com.springSecurity.JWT.Models.dtos.CreateUserDto;
import com.springSecurity.JWT.Models.dtos.LoginUserDto;
import com.springSecurity.JWT.Security.CustomUserDetailsService;
import com.springSecurity.JWT.Services.EmailService;
import com.springSecurity.JWT.Services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/test")
public class SecurityController {
    private final CustomUserDetailsService customUserDetailsService;
    private final EmailService emailService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityController(CustomUserDetailsService customUserDetailsService, EmailService emailService, UserService userService, PasswordEncoder passwordEncoder) {
        this.customUserDetailsService = customUserDetailsService;
        this.emailService = emailService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("message", "Добре дошли в моята електронна борса за продажба на млечни продукти!");
        return "homePage";
    }

    @GetMapping("/login")
    public String displayLoginPage() {
        return "login";
    }

    /*@PostMapping("/login")
    public String processLogin(@RequestParam String username, Model model) {
        try {
            String role = customUserDetailsService.loadUserByUsername(username).getAuthorities().iterator().next().getAuthority();

            if (role.equals("buyer")) {
                return "redirect:/test/buyer";
            } else if (role.equals("seller")) {
                return "redirect:/test/seller";
            }
            model.addAttribute("error", "Непозната роля.");
            return "login";
        } catch (AuthenticationException e) {
            model.addAttribute("error", "Невалидни данни за вход");
            return "login";
        }
    }*/

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


    @GetMapping("/register")
    public String displayCreateRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "registerPage";
    }

    @PostMapping("/register")
    public String createRegistration(@Valid @ModelAttribute("user") CreateUserDto createUserDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        customUserDetailsService.validateUser(createUserDto, bindingResult);
        if (bindingResult.hasErrors()) {
            return "registerPage";
        }

        customUserDetailsService.create(createUserDto);
        emailService.sendRegistrationEmail(createUserDto.getEmail(), createUserDto.getName());
        redirectAttributes.addFlashAttribute("success", "Registration successful!");
        return "redirect:/test/login";
    }

    @GetMapping("/seller")
    public String sellerPage(Model model) {
        model.addAttribute("seller", "Hello " + userService.getUserByUsername().getName());
        return "sellerPage";
    }

    @GetMapping("/buyer")
    public String buyerPage(Model model) {
        model.addAttribute("buyer", "Hello " + userService.getUserByUsername().getName());
        return "buyerPage";
    }

    @PostMapping("/logout")
    public String logout() {
        customUserDetailsService.deleteCookies();
        return "redirect:/test/home";
    }

    @GetMapping("/tokenExpiration")
    public String showInvalidToken() {
        customUserDetailsService.deleteCookies();
        return "tokenExpirationPage";
    }

}
