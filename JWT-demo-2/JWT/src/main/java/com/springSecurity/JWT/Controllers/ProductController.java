package com.springSecurity.JWT.Controllers;

import com.springSecurity.JWT.Models.Product;
import com.springSecurity.JWT.Models.dtos.CreateProductDto;
import com.springSecurity.JWT.Services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/all")
    public String getProductsByUsername(Model model) {
        List<Product> products = productService.getAllProductByUsername();
        model.addAttribute("productList", products);
        return "listOfProduct";

    }

    @PostMapping("/create")
    public ResponseEntity<?> createNewProduct(@Valid @RequestBody CreateProductDto createProductDto, BindingResult bindingResult) {

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
        productService.createProduct(createProductDto);
        return ResponseEntity.ok(Map.of("message", "Продуктът е създаден успешно"));
    }

    @GetMapping("/listToBuy")
    public String displayListToBuyPage() {
        return "listToBuyPage";
    }

    @PostMapping("/listToBuy")
    public String viewProductsByType(@RequestParam String productType, Model model) {
        model.addAttribute("productList", productService.getProductsByType(productType));
        return "listToBuyPage";
    }

}
