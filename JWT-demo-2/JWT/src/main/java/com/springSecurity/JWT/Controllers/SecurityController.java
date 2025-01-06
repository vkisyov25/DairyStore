package com.springSecurity.JWT.Controllers;

import com.springSecurity.JWT.Models.User;
import com.springSecurity.JWT.Security.CustomUserDetailsService;
import com.springSecurity.JWT.Services.EmailService;
import com.springSecurity.JWT.Utils.JwtUtil;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import jakarta.servlet.http.HttpServletResponse;
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
    @GetMapping("/login")
    public String showloginPage(Model model) {
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
                               @RequestParam String password,
                               Model model,HttpServletResponse response) {
        try {
            // Аутентикация
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            // Зареждане на данни за потребителя
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            // Генериране на JWT токен
            String token = jwtUtil.generateToken(userDetails);

            //TODO: I may be have to delete it.

            // Добавяне на токена в сесията или като атрибут
            model.addAttribute("token", token);

            String role = userDetails.getAuthorities().iterator().next().getAuthority();
            //Collection<? extends GrantedAuthority> cc = authentication.getAuthorities();

            // Съхраняване на JWT токен в HTTP-only cookie
            Cookie cookie = new Cookie("JWT", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(3600); // 1 час
            response.addCookie(cookie);

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
            return "login";
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
