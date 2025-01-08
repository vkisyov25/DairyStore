package com.springSecurity.JWT.Controllers;

import com.springSecurity.JWT.Models.Product;
import com.springSecurity.JWT.Services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    @PreAuthorize("hasAuthority('seller')")
    @GetMapping("/all")
    public String getAllProductsByUsername(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            String role = authentication.getAuthorities().iterator().next().getAuthority();
            List<Product> products = productService.getAllProductByUsername(username);
            model.addAttribute("productList", products);
            return "listOfProduct";
        }
        //TODO: Не може да я достъпим ако не сме логнати
        return "login"; // Ако няма логнат потребител
    }

}
