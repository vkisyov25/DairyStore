package com.dairystore.Controllers;

import com.dairystore.Models.User;
import com.dairystore.Models.dtos.UserInformationDto;
import com.dairystore.Services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

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
        User user = userService.getUserByUsername();
        userService.validateUserInformationDto(user,userInformationDto, bindingResult);
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .collect(Collectors.toMap(
                            FieldError::getField,
                            fieldError -> fieldError.getDefaultMessage(),
                            (existing, replacement) -> existing // Ако има повече от една грешка за дадено поле, вземи първата
                    ));

            return ResponseEntity.badRequest().body(Map.of("errors", errors));
        }

        userService.updateCurrentUserInformation(user,userInformationDto);
        return ResponseEntity.ok(Map.of("message", "User updated successfully"));
    }

    @DeleteMapping("/deleteCompany")
    public ResponseEntity<?> deleteCompanyInfo() {
        userService.deleteCompanyInfoOnTheCurrentUser();
        return ResponseEntity.ok(Collections.singletonMap("message", "Фирмените данни бяха изтрити."));
    }

}
