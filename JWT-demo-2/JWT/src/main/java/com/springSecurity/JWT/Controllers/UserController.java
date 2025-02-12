package com.springSecurity.JWT.Controllers;

import com.springSecurity.JWT.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/allInformation")
    public String getUserByUsername(Model model) {
        model.addAttribute("user", userService.getUserByUsername());
        return "userInformationPage";
    }
}
