package com.springSecurity.JWT.Controllers;

import com.springSecurity.JWT.Models.dtos.UserInformationDto;
import com.springSecurity.JWT.Services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /*@GetMapping("/allInformation")
    public String getUserByUsername(Model model) {
        model.addAttribute("user", userService.getUserByUsername());
        return "userInformationPage";
    }*/

    @GetMapping("/allInformation")
    public ResponseEntity<UserInformationDto> getCurrentUserInformation() {
        return ResponseEntity.ok(userService.getCurrentUserInformation());
    }

    @PostMapping("/edit")
    public ResponseEntity<?> editUserInformation(@Valid @RequestBody UserInformationDto userInformationDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Събиране на всички грешки в списък
            List<String> errors = bindingResult.getAllErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());

            // Връщане на грешките като JSON
            return ResponseEntity.badRequest().body(Map.of("errors", errors));
        }

        userService.updateCurrentUserInformation(userInformationDto);
        return ResponseEntity.ok(Map.of("message", "User updated successfully"));
    }

    @DeleteMapping("/deleteCompany")
    public ResponseEntity<?> deleteCompanyInfo() {
        userService.deleteCompanyInfoOnTheCurrentUser();
        return ResponseEntity.ok(Collections.singletonMap("message", "Фирмените данни бяха изтрити."));
    }

}
