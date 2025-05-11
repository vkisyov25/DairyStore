package com.dairystore.Controllers;

import com.dairystore.Models.DeliveryCompany;
import com.dairystore.Models.dtos.CreateDeliveryCompanyDto;
import com.dairystore.Services.DeliveryCompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/delivery-company")
public class DeliveryCompanyController {
    private final DeliveryCompanyService deliveryCompanyService;

    @GetMapping("/all")
    public ResponseEntity<List<DeliveryCompany>> viewAll() {
        return ResponseEntity.ok().body(deliveryCompanyService.getDeliveryCompanies());
    }

    @DeleteMapping("/deleteBy/{companyId}")
    public ResponseEntity<?> deleteByCompanyId(@PathVariable Long companyId) {
        try {
            deliveryCompanyService.deleteDeliveryCompanyById(companyId);
            return ResponseEntity.ok().body("Фирмата доставчик е изтрита успешно");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createDeliveryCompany(@Valid @RequestBody CreateDeliveryCompanyDto createDeliveryCompanyDto, BindingResult bindingResult) {

        deliveryCompanyService.isExistCompanyByName(createDeliveryCompanyDto.getName(), bindingResult);
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
        deliveryCompanyService.createDeliveryCompany(createDeliveryCompanyDto);
        return ResponseEntity.ok(Map.of("message", "Продуктът е създаден успешно"));
    }

}
