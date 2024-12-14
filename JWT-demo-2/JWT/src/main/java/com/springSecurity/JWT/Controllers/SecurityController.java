package com.springSecurity.JWT.Controllers;

import com.springSecurity.JWT.Models.User;
import com.springSecurity.JWT.Security.CustomUserDetailsService;
import com.springSecurity.JWT.Services.EmailService;
import com.springSecurity.JWT.Utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@Controller
//@RestController
@RequestMapping("/test")
public class SecurityController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;
    @Autowired
    private EmailService emailService;
    @Autowired
    public SecurityController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, CustomUserDetailsService customUserDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
    }

    @GetMapping("/home")
    // To work this the controller have to be only @Controller (not @RestController)
    public String home(Model model) {
        model.addAttribute("message", "Добре дошли в моята HTML страница!");
        return "homePage";
    }

    /*@GetMapping("/login")
    public String login (Model model){
        // Извличане на информация за логнатия потребител
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = authentication.getName();
        String role = authentication.getPrincipal().toString();
        if (role.equals("admin")){
            model.addAttribute("admin", "Hello " + loggedInUsername);
            return "adminPage";
        }
        model.addAttribute("seller","Hello " + loggedInUsername );
        return "sellerPage";
    }*/

    @GetMapping("/login")
    public String showloginPage(Model model) {
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
                               @RequestParam String password,
                               Model model) {
        try {
            // Аутентикация
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            // Зареждане на данни за потребителя
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            // Генериране на JWT токен
            String token = jwtUtil.generateToken(userDetails);

            // Добавяне на токена в сесията или като атрибут
            model.addAttribute("token", token);

            String role = userDetails.getAuthorities().iterator().next().getAuthority(); // Взима първата роля

            /*// Навигация според ролята
            switch (role) {
                case "seller":
                    return "redirect:/sellerPage"; // Пренасочва към sellerPage
                *//*case "bayer":
                    return "redirect:/buyerPage"; // Пренасочва към buyerPage*//*
                case "admin":
                    return "redirect:/adminPage"; // Пренасочва към adminPage
                default:
                    model.addAttribute("error", "Непозната роля.");
                    return "login";
            }*/
            if (role.equals("admin")){
                model.addAttribute("admin", "Hello " + username);
                return "adminPage";
            } else if (role.equals("seller")) {
                model.addAttribute("seller","Hello " + username );
                return "sellerPage";
            }
            model.addAttribute("error", "Непозната роля.");
            return "login";
        } catch (AuthenticationException e) {
            model.addAttribute("error", "Невалидни данни за вход");
            return "login"; // Връща страницата с грешка
        }
    }
    @GetMapping("/register")
    public String showCreateFrom(Model model){
        model.addAttribute("user",new User());
        return "registerPage";
    }

    @PostMapping("/register")
    public String create(@ModelAttribute User user){
        customUserDetailsService.create(user);
        emailService.sendRegistrationEmail(user.getEmail(), user.getUsername());
        return "redirect:/test/home";
    }
}
