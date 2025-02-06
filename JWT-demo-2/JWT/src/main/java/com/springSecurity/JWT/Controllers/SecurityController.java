package com.springSecurity.JWT.Controllers;

import com.springSecurity.JWT.Models.User;
import com.springSecurity.JWT.Security.CustomUserDetailsService;
import com.springSecurity.JWT.Services.EmailService;
import com.springSecurity.JWT.Utils.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String processLogin(@RequestParam String username, @RequestParam String password, Model model, HttpServletResponse response, HttpServletRequest request) {
        try {
            // Аутентикация
            //Authentication authentication = authenticationManager.authenticate(
            //new UsernamePasswordAuthenticationToken(username, password));

            // Зареждане на данни за потребителя
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            // Генериране на JWT токен
            String token = jwtUtil.generateToken(userDetails);

            //TODO: I may be have to delete it.

            // Добавяне на токена в сесията или като атрибут
            model.addAttribute("token", token);

            String role = userDetails.getAuthorities().iterator().next().getAuthority();
            //Collection<? extends GrantedAuthority> cc = authentication.getAuthorities();

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

            /*if (jwtUtil.isTokenExpired(token)) {
                token = jwtUtil.generateToken(customUserDetailsService.loadUserByUsername(username));}*/

            // Съхраняване на JWT токен в HTTP-only cookie
            Cookie cookie = new Cookie("JWT", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(3600); // 1 час
            response.addCookie(cookie);

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
    }

    @GetMapping("/register")
    public String showCreateFrom(Model model) {
        model.addAttribute("user", new User());
        return "registerPage";
    }

    @PostMapping("/register")
    public String create(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        //проверката с ролите е заради това, че при buyer companyName и companyEIK могат да са празни и като има потребител с празни
        // companyName и companyEIK в базата данни и като искам да запиша нов ми показва, че вече съществуват
        if (user.getAuthorities().equals("seller")) {
            //По принцип seller няма как да има записани потребители с празно companyName, но метода existsByCompanyName() проверява за
            //всички потребители а buyer могат да имат и затова слагам !user.getCompanyName().isEmpty()
            if (customUserDetailsService.existsByCompanyName(user.getCompanyName()) && !user.getCompanyName().isEmpty()) {
                bindingResult.rejectValue("companyName", "error.companyName", "Company already exists.");
            }
            
           if (customUserDetailsService.existsByCompanyEIK(user.getCompanyEIK()) && !user.getCompanyEIK().isEmpty()) {
                bindingResult.rejectValue("companyEIK", "error.companyEIK", "Company EIK already exists");
           }

        }

        if (user.getAuthorities().equals("buyer")) {
            if (customUserDetailsService.existsByCompanyName(user.getCompanyName()) && !user.getCompanyName().isEmpty()) {
                bindingResult.rejectValue("companyName", "error.companyName", "Company already exists.");
            }

            if (customUserDetailsService.existsByCompanyEIK(user.getCompanyEIK()) && !user.getCompanyEIK().isEmpty()) {
                bindingResult.rejectValue("companyEIK", "error.companyEIK", "Company EIK already exists");
            }

        }

        if (customUserDetailsService.existsByEmail(user.getEmail())) {
            bindingResult.rejectValue("email", "error.user", "Email already exists.");
        }

        if (bindingResult.hasErrors()) {
            return "registerPage";
        }

        customUserDetailsService.create(user);
        emailService.sendRegistrationEmail(user.getEmail(), user.getName());
        redirectAttributes.addFlashAttribute("success", "Registration successful!");
        return "redirect:/test/login";
    }

    @GetMapping("/seller")
    public String sellerPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("seller", "Hello " + authentication.getName());
        return "sellerPage";
    }

    @GetMapping("/buyer")
    public String buyerPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("buyer", "Hello " + authentication.getName());
        return "buyerPage";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {

        request.getSession().invalidate(); // Това ще изтрие текущата сесия

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("JWT")) {
                    cookie.setValue(null);
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie); // Изпращаме обратно изтритото cookie
                }
            }
        }

        return "redirect:/test/home";
    }

    @GetMapping("/tokenExpiration")
    public String showInvalidToken(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().invalidate(); // Това ще изтрие текущата сесия

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("JWT")) {
                    cookie.setValue(null);
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie); // Изпращаме обратно изтритото cookie
                }
            }
        }
        return "tokenExpirationPage";
    }
}
