package com.springSecurity.JWT.Controllers;

import com.springSecurity.JWT.Models.User;
import com.springSecurity.JWT.Security.CustomUserDetailsService;
import com.springSecurity.JWT.Services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
@Controller
//@RestController
@RequestMapping("/test")
public class SecurityController {
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private EmailService emailService;

    @Autowired
    public SecurityController(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @GetMapping("/home")
    // To work this the controller have to be only @Controller (not @RestController)
    public String home(Model model) {
        model.addAttribute("message", "Добре дошли в моята HTML страница!");
        return "homePage";
    }

    @GetMapping("/login")
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
