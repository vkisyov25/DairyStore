package com.springSecurity.JWT.Controllers;

import com.springSecurity.JWT.Models.Product;
import com.springSecurity.JWT.Services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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

    @GetMapping("/create")
    public String displayCreateProductPage(Model model) {
        model.addAttribute("product", new Product());
        return "createProductPage";
    }

    @PostMapping("/create")
    public ResponseEntity<?> createNewProduct(@RequestBody Product product) {
        productService.createProduct(product);
        return ResponseEntity.ok("Product created successfully!");
    }
   /* @PostMapping("/create")
    public String createProduct(@ModelAttribute Product product) {
        productService.creteProduct(product);
        return "sellerPage";
    }*/

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
