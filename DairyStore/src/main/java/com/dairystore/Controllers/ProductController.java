package com.dairystore.Controllers;

import com.dairystore.Models.dtos.CreateProductDto;
import com.dairystore.Models.dtos.ProductForSaleDto;
import com.dairystore.Models.dtos.ViewProductDto;
import com.dairystore.Services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/all")
    public ResponseEntity<List<ViewProductDto>> getCurrentUserProducts() {
        return ResponseEntity.ok().body(productService.getCurrentUserProducts());
    }

    @PostMapping("/create")
    public ResponseEntity<?> createNewProduct(@Valid @RequestBody CreateProductDto createProductDto, BindingResult bindingResult) {
        productService.isExist(createProductDto.getName(), bindingResult);
        if (bindingResult.hasErrors()) {

            Map<String, String> errors = bindingResult.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, fieldError -> fieldError.getDefaultMessage(), (existing, replacement) -> existing // Ако има повече от една грешка за дадено поле, вземи първата
            ));

            return ResponseEntity.badRequest().body(Map.of("errors", errors));
        }
        productService.createProduct(createProductDto);
        return ResponseEntity.ok(Map.of("message", "Продуктът е създаден успешно"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteById(id);
            return ResponseEntity.ok().body("Продуктът с ID " + id + " е изтрит успешно.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ViewProductDto> getProductById(@PathVariable Long id) {
        ViewProductDto byId = productService.getById(id);
        return ResponseEntity.ok(byId);
    }

    @PutMapping("/edit")
    public ResponseEntity<?> saveEditedProduct(@Valid @RequestBody ViewProductDto viewProductDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, fieldError -> fieldError.getDefaultMessage(), (existing, replacement) -> existing // Ако има повече от една грешка за дадено поле, вземи първата
            ));
            return ResponseEntity.badRequest().body(Map.of("errors", errors));
        }

        productService.saveEditedProduct(viewProductDto);
        return ResponseEntity.ok(Map.of("message", "Продуктът е успешно редактиран"));
    }

    @GetMapping("/for-sale")
    public ResponseEntity<List<ProductForSaleDto>> loadProductsForSale() {
        return ResponseEntity.ok().body(productService.getProductsForSale());
    }
}
